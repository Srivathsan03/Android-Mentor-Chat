package com.sri.androidmentorchat.moltbook

class MoltbookRepository {

    suspend fun postThread(
        postRequest: PostRequest
    ): PostResponse {
        return MoltbookRetrofitClient.apiService.createPost(postRequest)
    }
}