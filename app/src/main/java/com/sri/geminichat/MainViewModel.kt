package com.sri.geminichat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel : ViewModel() {

    val repository = MainRepository()

    private val _session = MutableStateFlow(
        ChatSession(
            chatId = UUID.randomUUID().toString(),
            messages = listOf()
        )
    )

    val session = _session.asStateFlow()

    fun sendMessage(
        prompt: String
    ) {
        val userMessage = ChatHistory(sender = Sender.USER, message = prompt)
        _session.update { it.copy(messages = it.messages + userMessage) }

        viewModelScope.launch {
            val fullPrompt = session.value.messages.joinToString("\n") { history ->
                "${history.sender.name}: ${history.message}"
            }

            _session.update {
                it.copy(
                    messages = it.messages + ChatHistory(
                        sender = Sender.GEMINI,
                        message = "Thinking..."
                    )
                )
            }

            repository.streamResponse(
                prompt = fullPrompt,
                model = AIModel.GEMINI_3_1_FLASH_LITE
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