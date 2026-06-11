package com.sri.geminichat

import android.util.Log
import com.google.genai.Client
import com.google.genai.errors.ClientException
import com.google.genai.types.GenerateContentConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository {

    private val client by lazy {
        Client.builder()
            .apiKey(BuildConfig.GEMINI_API_KEY)
            .build()
    }

    suspend fun geminiResponse(
        prompt: String
    ): String = withContext(Dispatchers.IO) {
        try {
            Log.d("TAG", "geminiResponse: request sent - $prompt")
            val response = client.models.generateContent(
                "gemini-2.5-flash",
                prompt,
                GenerateContentConfig.builder()
                    .build()
            )
            Log.d("TAG", "geminiResponse: ${response.text()}")
            return@withContext response.text() ?: ""
        } catch (e: ClientException) {
            e.printStackTrace()
            return@withContext "Client error ${e.code()}: ${e.message()}}"
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Error: ${e.message}"
        }
    }
}