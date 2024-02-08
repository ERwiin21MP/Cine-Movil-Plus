package com.erwiin21mp.cinemovilplus.domain.model

data class ContentInitModel(
    val id: Int,
    val title: String,
    val synopsis: String,
    val genres: List<String>,
    val duration: Int,
    val releaseDate: String,
    val verticalImageUrl: String,
    val horizontalImageUrl: String,
    val rating: Int,
    val platformsList: List<String>,
    val uploadDate: Long,
    val producersList: List<String>,
    val type: String,
    val keywords: String,
    val isCamQuality: Boolean
)