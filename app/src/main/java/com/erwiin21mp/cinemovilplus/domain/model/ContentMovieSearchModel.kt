package com.erwiin21mp.cinemovilplus.domain.model

data class ContentMovieSearchModel(
    var title: String? = null,
    var originalTitle: String? = null,
    var synopsis: String? = null,
    var productionCompanies: String? = null,
    var productionCountries: String? = null,
    var releaseDate: String? = null,
    var verticalImageURL: String? = null,
    var tagline: String? = null
)

data class CompanyModel(
    var company: String? = null
)

data class CountryModel(
    var country: String? = null
)