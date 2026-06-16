package com.sri.geminichat

interface Agent {
    val name: String
    val instruction: String
    val supportsDifficulty: Boolean
}

enum class AgentType(
    val agent: Agent
) {
    ANDROID_MENTOR(AndroidMentorService()),
    ANDROID_INTERVIEWER(AndroidInterviewerAgent()),
    KOTLIN_INTERVIEWER(KotlinInterviewerAgent()),
    ARCHITECTURE_INTERVIEWER(ArchitectureInterviewerAgent())
}

class AndroidMentorService : Agent {
    override val name = "Android Mentor"
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
    override val supportsDifficulty = false
}

class AndroidInterviewerAgent : Agent {
    override val name = "Android Interviewer"
    override val instruction = """
        You are a Senior Android Interviewer.

        Rules:
        - Ask one question at a time.
        - Wait for the candidate's answer.
        - Evaluate the answer.
        - Mention strengths and weaknesses.
        - Give a rating out of 10.
        - Ask the next question.
    """.trimIndent()
    override val supportsDifficulty = true
}

class KotlinInterviewerAgent : Agent {
    override val name = "Kotlin Interviewer"
    override val instruction = """
        You are a Senior Kotlin Interviewer.

        Rules:
        - Ask one Kotlin question at a time.
        - Wait for the candidate's answer.
        - Evaluate the answer.
        - Mention strengths and weaknesses.
        - Give a rating out of 10.
        - Ask the next question.
    """.trimIndent()
    override val supportsDifficulty = true
}

class ArchitectureInterviewerAgent : Agent {
    override val name = "Architecture Interviewer"
    override val instruction = """
        You are a Senior Android Architect.

        Rules:
        - Start with beginner questions.
        - Increase difficulty based on candidate responses.
        - Focus on:
          * MVVM
          * Clean Architecture
          * SOLID
          * Repository Pattern
          * Dependency Injection
          * Coroutines
          * Flow
          * Compose Architecture
        - Ask one question at a time.
        - Evaluate every answer.
        - Give a rating out of 10.
        - Explain the ideal answer briefly.
        - Ask the next question.
    """.trimIndent()
    override val supportsDifficulty = true
}

enum class DifficultyLevel {
    BEGINNER,
    INTERMEDIATE,
    SENIOR
}