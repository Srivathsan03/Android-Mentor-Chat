package com.sri.androidmentorchat

import android.util.Log
import com.google.genai.Client
import com.google.genai.errors.ClientException
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AiRepository {

    private val client by lazy {
        Client.builder()
            .apiKey(BuildConfig.GEMINI_API_KEY)
            .build()
    }

    fun streamResponse(
        agent: Agent,
        chatHistory: List<ChatHistory>,
        difficultyLevel: DifficultyLevel?
    ): Flow<String> = flow {
        val fullPrompt = chatHistory.joinToString("\n") { history ->
            "${history.senderType.name}: ${history.message}"
        }

        try {
            Log.d("TAG", "request sent - $fullPrompt")
            val stream = client.models.generateContentStream(
                AIModel.GEMINI_3_1_FLASH_LITE.modelId,
                fullPrompt,
                createConfig(agent, difficultyLevel)
            )
            for (chunk in stream) {
                emit(chunk.text() ?: "")
                Log.d("TAG", "geminiResponse: ${chunk.text()}")
            }
        } catch (e: ClientException) {
            e.printStackTrace()
            emit("Client error ${e.code()}: ${e.message()}}")
        } catch (e: Exception) {
            e.printStackTrace()
            emit("Error: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getGeminiSummary(
        chatHistory: List<ChatHistory>
    ): String = withContext(Dispatchers.IO) {
        val prompt = chatHistory.joinToString("\n") { history ->
            "${history.senderType.name}: ${history.message}"
        }
        try {
            val response = client.models.generateContent(
                AIModel.GEMINI_3_1_FLASH_LITE.modelId,
                prompt,
                GenerateContentConfig.builder().build()
            )
            return@withContext response.text() ?: ""
        } catch (e: ClientException) {
            e.printStackTrace()
            return@withContext "Client error (${e.code()}): ${e.message}"
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Error: ${e.message}"
        }
    }

    fun createConfig(
        agent: Agent,
        difficultyLevel: DifficultyLevel?
    ): GenerateContentConfig {
        val fullInstruction =
            if (difficultyLevel != null) {
                """
                    ${agent.instruction}
                    
                    Interviewer Difficulty: ${difficultyLevel.name}

                    Adjust questions according to the selected difficulty level.

                    BEGINNER:
                    - Ask basic concepts.
                    - Avoid tricky questions.

                    INTERMEDIATE:
                    - Ask practical and scenario-based questions.

                    SENIOR:
                    - Ask architecture, trade-offs, and system design questions.
                """.trimIndent()
            } else {
                agent.instruction
            }
        val config = GenerateContentConfig.builder()
            .systemInstruction(
                Content.fromParts(
                    Part.fromText(
                        fullInstruction
                    )
                )
            )
            .build()
        return config
    }
}