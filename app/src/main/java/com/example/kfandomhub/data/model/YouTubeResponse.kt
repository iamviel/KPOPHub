package com.example.kfandomhub.data.model

import com.google.gson.annotations.SerializedName

data class YouTubeSearchResponse(
    @SerializedName("nextPageToken")
    val nextPageToken: String? = null,
    @SerializedName("items")
    val items: List<YouTubeSearchItem> = emptyList()
)

data class YouTubeSearchItem(
    @SerializedName("id")
    val id: YouTubeSearchId? = null
)

data class YouTubeSearchId(
    @SerializedName("videoId")
    val videoId: String? = null
)

data class YouTubeVideosResponse(
    @SerializedName("items")
    val items: List<YouTubeVideo> = emptyList()
)

data class YouTubePlaylistItemsResponse(
    @SerializedName("nextPageToken")
    val nextPageToken: String? = null,
    @SerializedName("items")
    val items: List<YouTubePlaylistItem> = emptyList()
)

data class YouTubePlaylistItem(
    @SerializedName("contentDetails")
    val contentDetails: YouTubePlaylistContentDetails? = null,
    @SerializedName("snippet")
    val snippet: YouTubePlaylistSnippet? = null
)

data class YouTubePlaylistContentDetails(
    @SerializedName("videoId")
    val videoId: String? = null
)

data class YouTubePlaylistSnippet(
    @SerializedName("resourceId")
    val resourceId: YouTubeSearchId? = null
)

data class YouTubeVideo(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("snippet")
    val snippet: YouTubeSnippet? = null,
    @SerializedName("statistics")
    val statistics: YouTubeStatistics? = null
)

data class YouTubeSnippet(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("channelTitle")
    val channelTitle: String? = null,
    @SerializedName("publishedAt")
    val publishedAt: String? = null,
    @SerializedName("thumbnails")
    val thumbnails: YouTubeThumbnails? = null
)

data class YouTubeThumbnails(
    @SerializedName("default")
    val defaultImage: YouTubeThumbnail? = null,
    @SerializedName("medium")
    val medium: YouTubeThumbnail? = null,
    @SerializedName("high")
    val high: YouTubeThumbnail? = null,
    @SerializedName("standard")
    val standard: YouTubeThumbnail? = null,
    @SerializedName("maxres")
    val maxres: YouTubeThumbnail? = null
) {
    fun bestUrl(): String {
        return maxres?.url
            ?: standard?.url
            ?: high?.url
            ?: medium?.url
            ?: defaultImage?.url
            ?: ""
    }
}

data class YouTubeThumbnail(
    @SerializedName("url")
    val url: String? = null
)

data class YouTubeStatistics(
    @SerializedName("viewCount")
    val viewCount: String? = null
)
