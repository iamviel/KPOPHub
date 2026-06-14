package com.example.kfandomhub.data.di

import android.content.Context
import androidx.room.Room
import com.example.kfandomhub.data.database.AppDatabase
import com.example.kfandomhub.data.database.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "kfandom_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providePostDao(database: AppDatabase): PostDao = database.postDao()
}
