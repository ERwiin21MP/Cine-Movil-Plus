package com.erwiin21mp.cinemovilplus.domain.model

data class PlatformMovieModel(
    var results: ResultsMovieModel? = null
)

data class ResultsMovieModel(
    var mx: MXMovieModel? = null
)

data class MXMovieModel(
    var rent: List<ItemMXModel>? = null,
    var buy: List<ItemMXModel>? = null,
    var flatrate: List<ItemMXModel>? = null
)

data class ItemMXModel(val imageUrl: String? = null, val name: String? = null)