package com.sri.geminichat

data class ChatHistory(
    val sender: Sender,
    var message: String
)

enum class Sender {
    USER,
    GEMINI
}