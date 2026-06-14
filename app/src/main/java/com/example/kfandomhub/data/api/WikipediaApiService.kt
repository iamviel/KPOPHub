package com.example.kfandomhub.data.api

import com.example.kfandomhub.data.model.WikiSummary
import retrofit2.http.GET
import retrofit2.http.Path

interface WikipediaApiService {
    @GET("page/summary/{title}")
    suspend fun getPageSummary(@Path("title") title: String): WikiSummary
}
