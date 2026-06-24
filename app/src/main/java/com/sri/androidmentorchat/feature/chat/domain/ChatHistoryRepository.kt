package com.sri.androidmentorchat.feature.chat.domain

import com.sri.androidmentorchat.core.database.MessageDao
import com.sri.androidmentorchat.core.database.MessageEntity
import kotlinx.coroutines.flow.Flow

class ChatHistoryRepository(
    private val messageDao: MessageDao
) {

    fun getAllMessages(): Flow<List<MessageEntity>> {
        return messageDao.getAllMessages()
    }

    suspend fun insertMessages(messageEntity: MessageEntity) {
        messageDao.insertMessage(messageEntity)
    }

    suspend fun clearChat() {
        messageDao.deleteAllMessages()
    }
}