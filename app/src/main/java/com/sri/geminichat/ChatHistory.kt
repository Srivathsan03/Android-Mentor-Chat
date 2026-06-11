package com.sri.geminichat

data class ChatHistory(
    val sender: Sender,
    val message: String
)

enum class Sender {
    USER,
    GEMINI
}