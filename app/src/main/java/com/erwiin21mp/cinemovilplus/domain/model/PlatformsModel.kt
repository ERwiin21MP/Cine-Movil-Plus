package com.erwiin21mp.cinemovilplus.domain.model

data class PlatformsModel(
    var results: ResultsModel? = null
)

data class ResultsModel(
    var mx: MxModel? = null
)

data class MxModel(
    var flatrate: List<FlatrateModel>? = null
)

data class FlatrateModel(
    var imageURL: String? = null,
    var name: String? = null,
    var displayPriority: Int? = null
)
