package com.erwiin21mp.cinemovilplus.domain.model

data class ContentSearchModel(
    var id: String? = null,
    var keywords: String? = null,
    var genres: String? = null,
    var type: Type? = null,
    var uploadDate: Long? = null,
    var isCameraQuality: Boolean? = null,

    var title: String? = null,
    var originalTitle: String? = null,
    var overview: String? = null,
    var productionCompanies: String? = null,
    var productionCountries: String? = null,
    var releaseDate: String? = null,
    var verticalImageURL: String? = null,
    var createdBy: String? = null,
    var networks: String? = null,
    var tagline: String? = null,
    var country: String? = null
)