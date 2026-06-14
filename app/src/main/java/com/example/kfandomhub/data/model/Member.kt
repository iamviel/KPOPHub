package com.example.kfandomhub.data.model

data class Member(
    val id: Int,
    val groupId: Int,
    val name: String,
    val position: String,
    val wikiTitle: String,
    val photoUrl: String,
    val stageName: String = name,
    val birthName: String = "",
    val birthday: String = "",
    val zodiacSign: String = "",
    val chineseZodiacSign: String = "",
    val height: String = "",
    val weight: String = "",
    val bloodType: String = "",
    val mbtiType: String = "",
    val nationality: String = "",
    val birthplace: String = "",
    val instagram: String = "",
    val sourceUrl: String = ""
)
