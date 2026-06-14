package com.example.kfandomhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageUri: String,
    val title: String,
    val caption: String,
    val timestamp: Long = System.currentTimeMillis(),
    val authorName: String = "Anonymous",
    val authorUsername: String = "anonymous",
    val authorImageUri: String? = null,
    val tags: String = "",
    val likeCount: Int = 0,
    val isLiked: Boolean = false
)

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postId: Int,
    val authorName: String = "Anonymous",
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
