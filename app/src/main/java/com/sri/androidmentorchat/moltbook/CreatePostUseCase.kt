package com.sri.androidmentorchat.moltbook

class CreatePostUseCase(
    private val repository: MoltbookRepository
) {
    suspend operator fun invoke(title: String, content: String): PostResponse {
        return repository.postThread(
            PostRequest(
                submolt_name = "android",
                title = title,
                content = content
            )
        )
    }
}