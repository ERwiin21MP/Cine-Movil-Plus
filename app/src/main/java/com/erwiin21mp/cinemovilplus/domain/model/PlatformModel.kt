package com.erwiin21mp.cinemovilplus.domain.model

data class PlatformModel(
    var results: ResultsModel?
)

data class ResultsModel(
    var mx: MXModel
)

data class MXModel(
    var rent: List<ItemMXModel>,
    var buy: List<ItemMXModel>,
    var flatrate: List<ItemMXModel>
)

data class ItemMXModel(
    val logoPath: String, val providerName: String
)