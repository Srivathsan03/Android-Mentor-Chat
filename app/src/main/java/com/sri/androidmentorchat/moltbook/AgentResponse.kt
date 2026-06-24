package com.sri.androidmentorchat.moltbook

data class AgentResponse(
    val success: Boolean,
    val agent: Agent
)

data class Agent(
    val name: String,
    val display_name: String,
    val description: String
)
