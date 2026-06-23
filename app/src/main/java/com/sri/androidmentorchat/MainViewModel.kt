package com.sri.androidmentorchat

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(
    private val aiRepository: AiRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                val aiRepository = AiRepository()
                val chatRepository = ChatRepository(
                    MentorDatabase.getDatabase(application).messageDao()
                )
                MainViewModel(aiRepository, chatRepository)
            }
        }
    }

    private val _selectedAgent: MutableStateFlow<Agent> =
        MutableStateFlow(AgentType.ANDROID_MENTOR.agent)
    val selectedAgent: StateFlow<Agent> = _selectedAgent.asStateFlow()

    private val _difficultyLevel: MutableStateFlow<DifficultyLevel?> =
        MutableStateFlow(DifficultyLevel.BEGINNER)
    val difficultyLevel: StateFlow<DifficultyLevel?> = _difficultyLevel.asStateFlow()

    private val _chatSession = MutableStateFlow(
        ChatSession(
            chatId = UUID.randomUUID().toString(),
            messages = listOf()
        )
    )
    val chatSession = _chatSession.asStateFlow()

    var chatRunner: ChatRunner = ChatRunner(
        repository = aiRepository,
        agent = _selectedAgent.value
    )

    fun selectAgent(agentType: AgentType) {
        _selectedAgent.value = agentType.agent
        if (!agentType.agent.supportsDifficulty)
            _difficultyLevel.value = null
        _chatSession.value = ChatSession(
            chatId = UUID.randomUUID().toString(),
            messages = listOf()
        )
        chatRunner = ChatRunner(
            repository = aiRepository,
            agent = agentType.agent
        )
    }

    fun selectDifficulty(level: DifficultyLevel) {
        if (_difficultyLevel.value != level) {
            _difficultyLevel.value = level
            _chatSession.value = ChatSession(
                chatId = UUID.randomUUID().toString(),
                messages = listOf()
            )
        }
    }

    fun updateChatSession(chatHistoryList: List<ChatHistory>) {
        _chatSession.update {
            it.copy(messages = chatHistoryList)
        }
    }
    fun sendMessage(
        prompt: String
    ) {
        val userMessage = ChatHistory(senderType = SenderType.USER, message = prompt)
        _chatSession.update { it.copy(messages = it.messages + userMessage) }
        saveMessage(text = prompt, sender = SenderType.USER.name)

        val responseBuilder = StringBuilder()
        viewModelScope.launch {
            _chatSession.update {
                it.copy(
                    messages = it.messages + ChatHistory(
                        senderType = SenderType.GEMINI,
                        message = "Thinking..."
                    )
                )
            }

            chatRunner.streamResponse(
                model = AIModel.GEMINI_3_1_FLASH_LITE,
                session = _chatSession.value,
                difficultyLevel = _difficultyLevel.value
            ).collect { chunk ->
                if (chunk.isNotEmpty()) {
                    responseBuilder.append(chunk)
                    _chatSession.update { session ->
                        val newList = session.messages.toMutableList()
                        val lastIndex = newList.lastIndex
                        if (lastIndex >= 0 && newList[lastIndex].senderType == SenderType.GEMINI) {
                            val currentMessage = newList[lastIndex].message
                            newList[lastIndex] =
                                if (currentMessage == "Thinking...")
                                    newList[lastIndex].copy(message = chunk)
                                else
                                    newList[lastIndex].copy(message = currentMessage + chunk)
                        }
                        session.copy(messages = newList)
                    }
                }
            }
            val response = responseBuilder.toString()
            saveMessage(text = response, sender = SenderType.GEMINI.name)
        }
    }

    val messages = chatRepository.getAllMessages()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    fun saveMessage(text: String, sender: String) {
        viewModelScope.launch {
            val messageEntity = MessageEntity(
                message = text,
                sender = sender,
                timeStamp = System.currentTimeMillis()
            )
            chatRepository.insertMessages(messageEntity)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            chatRepository.clearChat()
        }
    }
}