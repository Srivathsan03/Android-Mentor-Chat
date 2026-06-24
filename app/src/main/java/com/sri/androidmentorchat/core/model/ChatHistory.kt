package com.sri.androidmentorchat.core.model

data class ChatHistory(
    val senderType: SenderType,
    var message: String
)