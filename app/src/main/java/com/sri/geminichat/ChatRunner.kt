package com.sri.geminichat

import kotlinx.coroutines.flow.Flow

class ChatRunner(
    private val repository: MainRepository,
    private val agent: Agent
) {

    fun streamResponse(
        model: AIModel,
        session: ChatSession
    ): Flow<String> {

        return repository.streamResponse(
            model = model,
            instruction = agent.instruction,
            chatHistory = session.messages
        )
    }
}