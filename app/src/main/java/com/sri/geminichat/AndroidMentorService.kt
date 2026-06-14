package com.sri.geminichat

import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part

class AndroidMentorService : Agent {

    override val instruction = """
        You are a Senior Android Mentor.
        Rules:
        - Answer Android questions only.
        - Explain concepts clearly.
        - Give Kotlin examples.
        - Keep answers interview-oriented.
        - If question is unrelated to Android,
          politely refuse.
    """.trimIndent()

    override fun createConfig(): GenerateContentConfig {
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