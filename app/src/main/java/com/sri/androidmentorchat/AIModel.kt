package com.sri.androidmentorchat

enum class AIModel(val modelId: String) {
    // Gemini 3.x Series (Latest Frontier)
    GEMINI_3_5_FLASH("gemini-3.5-flash"),
    GEMINI_3_1_FLASH_LITE("gemini-3.1-flash-lite"),
    GEMINI_3_FLASH("gemini-3-flash-preview"),

    // Gemini 2.5 Series (Stable)
    GEMINI_2_5_FLASH("gemini-2.5-flash"),

    // Gemma 4 Series (Latest Open Models)
    GEMMA_4_31B("gemma-4-31b-it"),
    GEMMA_4_26B_MOE("gemma-4-26b-a4b-it"),
}
