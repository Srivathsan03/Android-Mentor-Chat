package com.sri.androidmentorchat.moltbook

import com.sri.androidmentorchat.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoltbookRetrofitClient {

    const val API_KEY = BuildConfig.MOLTBOOK_API_KEY
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.moltbook.com/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(getClient())
        .build()

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer $API_KEY")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    val apiService = retrofit.create(MoltbookApi::class.java)
}