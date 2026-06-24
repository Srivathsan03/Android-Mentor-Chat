package com.sri.androidmentorchat

import kotlinx.coroutines.flow.Flow

class ChatRunner(
    private val repository: AiRepository,
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