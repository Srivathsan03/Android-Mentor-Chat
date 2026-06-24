package com.sri.androidmentorchat.feature.moltbook.data

class MoltbookRepository {

    suspend fun postThread(
        postRequest: PostRequest
    ): PostResponse {
        return MoltbookRetrofitClient.apiService.createPost(postRequest)
    }
}