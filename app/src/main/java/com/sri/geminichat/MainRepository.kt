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

    private val instruction = """
        You are a Senior Android Mentor.
        Rules:
        - Answer Android questions only.
        - Explain concepts clearly.
        - Give Kotlin examples.
        - Keep answers interview-oriented.
        - If question is unrelated to Android,
          politely refuse.
    """.trimIndent()

    fun streamResponse(
        prompt: String,
        model: AIModel = AIModel.GEMINI_3_1_FLASH_LITE
    ): Flow<String> = flow {
        try {
            Log.d("TAG", "request sent - $prompt")
            val stream = client.models.generateContentStream(
                model.modelId,
                prompt,
                createConfig()
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

    private fun createConfig(): GenerateContentConfig {
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