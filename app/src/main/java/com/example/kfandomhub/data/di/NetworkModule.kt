package com.example.kfandomhub.data.di

import com.example.kfandomhub.data.api.WikipediaApiService
import com.example.kfandomhub.data.api.YouTubeApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://en.wikipedia.org/api/rest_v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideWikipediaApiService(retrofit: Retrofit): WikipediaApiService =
        retrofit.create(WikipediaApiService::class.java)

    @Provides
    @Singleton
    fun provideYouTubeApiService(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): YouTubeApiService = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/youtube/v3/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(YouTubeApiService::class.java)
}
