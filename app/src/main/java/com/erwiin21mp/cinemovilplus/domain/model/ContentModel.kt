package com.erwiin21mp.cinemovilplus.domain.model

data class ContentModel(
    var id: Int = 0,
    var title: String = "",
    var synopsis: String = "",
    var genres: List<String> = emptyList(),
    var duration: Int = 0,
    var director: String = "",
    var releaseDate: String = "",
    var classification: String = "",
    var verticalImageUrl: String = "",
    var horizontalImageUrl: String = "",
    var trailerUrl: String = "",
    var contentUrl: String = "",
    var rating: Int = 0,
    var platformsList: List<String> = emptyList(),
    var uploadDate: Long = 0L,
    var producersList: List<String> = emptyList(),
    var type: String = "",
    var distribution: String = "",
    var keywords: String = "",
    var isCamQuality: Boolean = false
)