package com.example.kfandomhub.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kfandomhub.data.model.Post
import com.example.kfandomhub.data.model.Comment

@Database(entities = [Post::class, Comment::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
