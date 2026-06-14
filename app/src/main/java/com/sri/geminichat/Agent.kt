package com.sri.geminichat

import com.google.genai.types.GenerateContentConfig

interface Agent {

    val instruction: String
    fun createConfig(): GenerateContentConfig
}