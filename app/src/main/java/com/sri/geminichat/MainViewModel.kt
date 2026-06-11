package com.sri.geminichat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val repository = MainRepository()

    val chatHistory = MutableStateFlow<List<ChatHistory>>(emptyList())
    val isEnabled = MutableStateFlow<Boolean>(true)

    fun sendMessage(
        prompt: String
    ) {
        val userMessage = ChatHistory(sender = Sender.USER, message = prompt)
        chatHistory.update {
            it+userMessage
        }
        isEnabled.value = false

        viewModelScope.launch {
            val resp = repository.geminiResponse(prompt)
            val geminiMessage = ChatHistory(sender = Sender.GEMINI, message = resp)
            chatHistory.update {
                it+geminiMessage
            }
            isEnabled.value = true
        }
    }
}