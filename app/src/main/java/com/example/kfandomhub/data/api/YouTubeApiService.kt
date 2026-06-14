package com.example.kfandomhub.data.api

import com.example.kfandomhub.data.model.YouTubeSearchResponse
import com.example.kfandomhub.data.model.YouTubePlaylistItemsResponse
import com.example.kfandomhub.data.model.YouTubeVideosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("videoCategoryId") videoCategoryId: String = "10",
        @Query("order") order: String,
        @Query("publishedAfter") publishedAfter: String? = null,
        @Query("maxResults") maxResults: Int = 20,
        @Query("regionCode") regionCode: String = "KR",
        @Query("pageToken") pageToken: String? = null,
        @Query("key") apiKey: String
    ): YouTubeSearchResponse

    @GET("videos")
    suspend fun getVideos(
        @Query("part") part: String = "snippet,statistics",
        @Query("id") ids: String,
        @Query("key") apiKey: String
    ): YouTubeVideosResponse

    @GET("playlistItems")
    suspend fun getPlaylistItems(
        @Query("part") part: String = "snippet,contentDetails",
        @Query("playlistId") playlistId: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("pageToken") pageToken: String? = null,
        @Query("key") apiKey: String
    ): YouTubePlaylistItemsResponse
}
