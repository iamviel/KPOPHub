package com.example.kfandomhub.data.model

data class TrendingItem(
    val videoId: String,
    val title: String,
    val groupName: String,
    val viewCount: Long,
    val viewText: String,
    val releasedAgo: String,
    val publishedAtMillis: Long,
    val thumbnailUrl: String,
    val youtubeUrl: String
)

enum class MusicRadarCategory {
    NEW_RELEASE,
    TRENDING_MV,
    MOST_VIEWED_MV
}

data class MusicRadarPage(
    val items: List<TrendingItem>,
    val nextPageToken: String?
)
