package com.example.kfandomhub.data.model

import com.google.gson.annotations.SerializedName

data class WikiSummary(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("extract")
    val extract: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: WikiImage? = null,
    @SerializedName("originalimage")
    val originalImage: WikiImage? = null
)

data class WikiImage(
    @SerializedName("source")
    val source: String? = null
)
