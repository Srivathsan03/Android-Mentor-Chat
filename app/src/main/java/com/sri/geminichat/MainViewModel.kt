package com.sri.geminichat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel : ViewModel() {

    private val _selectedAgent: MutableStateFlow<Agent> =
        MutableStateFlow(AgentType.ANDROID_MENTOR.agent)
    val selectedAgent: StateFlow<Agent> = _selectedAgent.asStateFlow()

    private val _difficultyLevel: MutableStateFlow<DifficultyLevel?> =
        MutableStateFlow(DifficultyLevel.BEGINNER)
    val difficultyLevel: StateFlow<DifficultyLevel?> = _difficultyLevel.asStateFlow()

    private val _session = MutableStateFlow(
        ChatSession(
            chatId = UUID.randomUUID().toString(),
            messages = listOf()
        )
    )
    val session = _session.asStateFlow()

    val repository = MainRepository()
    var chatRunner: ChatRunner = ChatRunner(
        repository = repository,
        agent = _selectedAgent.value
    )

    fun selectAgent(agentType: AgentType) {
        _selectedAgent.value = agentType.agent
        if(!agentType.agent.supportsDifficulty)
            _difficultyLevel.value = null
        _session.value = ChatSession(
            chatId = UUID.randomUUID().toString(),
            messages = listOf()
        )
        chatRunner = ChatRunner(
            repository = repository,
            agent = agentType.agent
        )
    }

    fun selectDifficulty(level: DifficultyLevel) {
        if(_difficultyLevel.value != level) {
            _difficultyLevel.value = level
            _session.value = ChatSession(
                chatId = UUID.randomUUID().toString(),
                messages = listOf()
            )
        }
    }

    fun sendMessage(
        prompt: String
    ) {
        val userMessage = ChatHistory(sender = Sender.USER, message = prompt)
        _session.update { it.copy(messages = it.messages + userMessage) }

        viewModelScope.launch {
            _session.update {
                it.copy(
                    messages = it.messages + ChatHistory(
                        sender = Sender.GEMINI,
                        message = "Thinking..."
                    )
                )
            }

            chatRunner.streamResponse(
                model = AIModel.GEMINI_3_1_FLASH_LITE,
                session = _session.value,
                difficultyLevel = _difficultyLevel.value
            ).collect { chunk ->
                if (chunk.isNotEmpty()) {
                    _session.update { session ->
                        val newList = session.messages.toMutableList()
                        val lastIndex = newList.lastIndex
                        if (lastIndex >= 0 && newList[lastIndex].sender == Sender.GEMINI) {
                            val currentMessage = newList[lastIndex].message
                            newList[lastIndex] =
                                if (currentMessage == "Thinking...")
                                    newList[lastIndex].copy(message = chunk)
                                else
                                    newList[lastIndex].copy(message = currentMessage + chunk)
                        }
                        _session.value.copy(messages = newList)
                    }
                }
            }
        }
    }
}