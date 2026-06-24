package com.sri.androidmentorchat.feature.chat.domain

import com.sri.androidmentorchat.feature.chat.data.GeminiRepository
import com.sri.androidmentorchat.core.model.Agent
import com.sri.androidmentorchat.core.model.ChatSession
import com.sri.androidmentorchat.core.model.DifficultyLevel
import kotlinx.coroutines.flow.Flow

class ChatRunner(
    private val repository: GeminiRepository,
    private val agent: Agent
) {

    fun streamResponse(
        session: ChatSession,
        difficultyLevel: DifficultyLevel?
    ): Flow<String> {
        return repository.streamResponse(
            agent = agent,
            chatHistory = session.messages,
            difficultyLevel = difficultyLevel
        )
    }

    suspend fun getGeminiSummary(
        session: ChatSession
    ): String {
        return repository.getGeminiSummary(session.messages)
    }
}