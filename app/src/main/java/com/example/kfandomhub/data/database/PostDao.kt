package com.example.kfandomhub.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kfandomhub.data.model.Post
import com.example.kfandomhub.data.model.Comment

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    suspend fun getAllPosts(): List<Post>

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun getPostCount(): Int

    @Insert
    suspend fun insertPost(post: Post)

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    suspend fun getCommentsForPost(postId: Int): List<Comment>

    @Insert
    suspend fun insertComment(comment: Comment)

    @Query("UPDATE posts SET likeCount = :likeCount, isLiked = :isLiked WHERE id = :postId")
    suspend fun updateLike(postId: Int, likeCount: Int, isLiked: Boolean)

    @Query("DELETE FROM comments")
    suspend fun deleteAllComments()

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()
}
