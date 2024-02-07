package com.erwiin21mp.cinemovilplus.domain.model

data class ContentInitModel(
    val id: Int,
    val title: String,
    val synopsis: String,
    val genres: List<String>,
    val duration: Int,
//    val director: String,
    val releaseDate: String,
//    val classification: String,
    val verticalImageUrl: String,
    val horizontalImageUrl: String,
//    val trailerUrl: String,
//    val contentUrl: String,
    val rating: Int,
    val platformsList: List<String>,
    val uploadDate: Int,
    val producersList: List<String>,
    val type: String,
//    val distribution: String,
    val keywords: String,
    val isCamQuality: Boolean
)