package com.sri.androidmentorchat.feature.moltbook.domain

import com.sri.androidmentorchat.feature.moltbook.data.MoltbookRepository
import com.sri.androidmentorchat.feature.moltbook.data.PostRequest
import com.sri.androidmentorchat.feature.moltbook.data.PostResponse

class CreatePostUseCase(
    private val repository: MoltbookRepository
) {
    suspend fun createPost(
        title: String,
        content: String
    ): PostResponse {
        return repository.postThread(
            PostRequest(
                submolt_name = "android",
                title = title,
                content = content
            )
        )
    }
}