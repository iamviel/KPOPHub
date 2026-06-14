package com.example.kfandomhub.data.model

data class Group(
    val id: Int,
    val name: String,
    val agency: String,
    val debut: Int,
    val generation: String,
    val fandomName: String,
    val wikiTitle: String,
    val imageUrl: String,
    val fanCount: String,
    val members: List<Member> = emptyList()
)
