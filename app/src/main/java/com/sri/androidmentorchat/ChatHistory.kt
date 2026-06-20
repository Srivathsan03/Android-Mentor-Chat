package com.sri.androidmentorchat

data class ChatHistory(
    val sender: Sender,
    var message: String
)

enum class Sender {
    USER,
    GEMINI
}