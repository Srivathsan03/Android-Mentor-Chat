package com.sri.androidmentorchat

data class ChatSession(
    val chatId: String,
    val messages: List<ChatHistory>
)