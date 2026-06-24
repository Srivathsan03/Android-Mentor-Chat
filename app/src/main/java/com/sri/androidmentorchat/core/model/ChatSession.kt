package com.sri.androidmentorchat.core.model

data class ChatSession(
    val chatId: String,
    val messages: List<ChatHistory>
)