package com.erwiin21mp.cinemovilplus.domain.model

data class PlatformMovieModel(
    var results: ResultsModel? = null
)

data class ResultsModel(
    var mx: MXMovieModel? = null
)

data class MXMovieModel(
    var rent: List<ItemMXModel>? = null,
    var buy: List<ItemMXModel>? = null,
    var flatrate: List<ItemMXModel>? = null
)

data class ItemMXModel(val imageUrl: String? = null, val name: String? = null)