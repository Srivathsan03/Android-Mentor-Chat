package com.sri.androidmentorchat

import kotlinx.coroutines.flow.Flow

class ChatRunner(
    private val repository: AiRepository,
    private val agent: Agent
) {

    fun streamResponse(
        model: AIModel,
        session: ChatSession,
        difficultyLevel: DifficultyLevel?
    ): Flow<String> {
        return repository.streamResponse(
            model = model,
            agent = agent,
            chatHistory = session.messages,
            difficultyLevel = difficultyLevel
        )
    }
}