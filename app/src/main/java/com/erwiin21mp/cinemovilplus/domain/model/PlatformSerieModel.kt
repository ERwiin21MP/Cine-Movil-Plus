package com.erwiin21mp.cinemovilplus.domain.model

data class PlatformSerieModel(var results: ResultsSerieModel? = null)
data class ResultsSerieModel(var mx: MXSerieModel? = null)
data class MXSerieModel(var flatrate: List<ItemMXModel>? = null)