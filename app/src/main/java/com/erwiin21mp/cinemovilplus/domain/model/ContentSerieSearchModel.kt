package com.erwiin21mp.cinemovilplus.domain.model

data class ContentSerieSearchModel(
    var title: String? = null,
    var originalTitle: String? = null,
    var synopsis: String? = null,
    var productionCompanies: String? = null,
    var productionCountries: String? = null,
    var releaseDate: String? = null,
    var verticalImageURL: String? = null,
    var createdBy: List<CreatedModel>,
    var networks: List<NetworkModel>,
    var tagline: String? = null,
    var country: List<String>
)

data class CreatedModel(
    var name: String? = null
)

data class NetworkModel(
    var name: String? = null
)