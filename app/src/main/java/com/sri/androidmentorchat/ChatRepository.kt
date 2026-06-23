package com.sri.androidmentorchat

import kotlinx.coroutines.flow.Flow

class ChatRepository(
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