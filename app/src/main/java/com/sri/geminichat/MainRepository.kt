package com.sri.geminichat

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

class MainRepository {

    private val client by lazy {
        Client.builder()
            .apiKey(BuildConfig.GEMINI_API_KEY)
            .build()
    }

    fun streamResponse(
        model: AIModel,
        instruction: String,
        chatHistory: List<ChatHistory>
    ): Flow<String> = flow {
        val fullPrompt = chatHistory.joinToString("\n") { history ->
            "${history.sender.name}: ${history.message}"
        }

        try {
            Log.d("TAG", "request sent - $fullPrompt")
            val stream = client.models.generateContentStream(
                model.modelId,
                fullPrompt,
                createConfig(instruction)
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

    fun createConfig(instruction: String): GenerateContentConfig {
        val config = GenerateContentConfig.builder()
            .systemInstruction(
                Content.fromParts(
                    Part.fromText(
                        instruction
                    )
                )
            )
            .build()
        return config
    }
}