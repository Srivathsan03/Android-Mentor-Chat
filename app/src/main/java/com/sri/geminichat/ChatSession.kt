package com.sri.geminichat

data class ChatSession(
    val chatId: String,
    val messages: List<ChatHistory>
)