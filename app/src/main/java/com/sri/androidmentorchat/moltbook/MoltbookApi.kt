package com.sri.androidmentorchat.moltbook

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MoltbookApi {

    @GET("agents/me")
    suspend fun getAgent(): AgentResponse

    @POST("posts")
    suspend fun createPost(
        @Body postRequest: PostRequest
    ): PostResponse
}