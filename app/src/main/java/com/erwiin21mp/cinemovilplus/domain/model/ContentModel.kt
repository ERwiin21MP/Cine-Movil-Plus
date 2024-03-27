package com.erwiin21mp.cinemovilplus.domain.model

data class ContentModel(
    // Firebase
    var contentURL: String? = null,
    var id: Int? = null,
    var idCollection: Int? = null,
    var idTmdb: Int? = null,
    var isCameraQuality: Boolean? = null,
    var isEnabled: Boolean? = null,
    var type: Type,
    var uploadDate: Long? = null,
    var genres: List<String>? = null,
    //Retrofit
    var horizontalImageURL: String? = null,
    var releaseDate: String? = null,
    var verticalImageURL: String? = null,
    var title: String? = null,
    var synopsis: String? = null,
    var productionCountries: List<String>? = null,
    var originalTitle: String? = null,
    var productionCompanies: List<CompanyModel>? = null,
    var createdBy: List<CreatedModel>? = null,
    var networks: List<NetworkModel>? = null,
    var tagline: String? = null,
    var platforms: List<FlatrateModel>? = null,
    //Firebase
    var keywords: String? = null
)

data class CreatedModel(
    var id: Int? = null,
    var name: String? = null,
    var profilePath: String? = null
)

data class NetworkModel(
    var id: Int? = null,
    var logoPath: String? = null,
    var name: String? = null
)

data class CompanyModel(
    var id: Int? = null,
    var logo: String? = null,
    var company: String? = null
)