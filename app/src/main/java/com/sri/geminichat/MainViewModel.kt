package com.sri.geminichat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val repository = MainRepository()

    val chatHistory = MutableStateFlow<List<ChatHistory>>(emptyList())

    fun sendMessage(
        prompt: String
    ) {
        val userMessage = ChatHistory(sender = Sender.USER, message = prompt)
        chatHistory.update { it + userMessage }

        viewModelScope.launch {
            val fullPrompt = chatHistory.value.joinToString("\n") { history ->
                "${history.sender.name}: ${history.message}"
            }

            chatHistory.update { it + ChatHistory(sender = Sender.GEMINI, message = "Thinking...") }

            repository.streamResponse(
                prompt = fullPrompt,
                model = AIModel.GEMINI_3_1_FLASH_LITE
            ).collect { chunk ->
                if (chunk.isNotEmpty()) {
                    chatHistory.update { list ->
                        val newList = list.toMutableList()
                        val lastIndex = newList.lastIndex
                        if (lastIndex >= 0 && newList[lastIndex].sender == Sender.GEMINI) {
                            val currentMessage = newList[lastIndex].message
                            newList[lastIndex] =
                                if (currentMessage == "Thinking...")
                                    newList[lastIndex].copy(message = chunk)
                                else
                                    newList[lastIndex].copy(message = currentMessage + chunk)
                        }
                        newList
                    }
                }
            }
        }
    }
}