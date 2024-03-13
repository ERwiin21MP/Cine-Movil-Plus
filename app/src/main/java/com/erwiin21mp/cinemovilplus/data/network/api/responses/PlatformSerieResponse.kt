package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.core.ext.toImageURL
import com.erwiin21mp.cinemovilplus.domain.model.ItemMXModel
import com.erwiin21mp.cinemovilplus.domain.model.MXSerieModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformSerieModel
import com.erwiin21mp.cinemovilplus.domain.model.ResultsSerieModel
import com.google.gson.annotations.SerializedName

data class PlatformSerieResponse(
    @SerializedName("results") var results: ResultsSerieResponse? = null
) {
    fun toDomain() = PlatformSerieModel(results = results?.toDomain())
}

data class ResultsSerieResponse(
    @SerializedName("MX") var mx: MXSerieResponse? = null
) {
    fun toDomain() = ResultsSerieModel(mx = mx?.toDomain())
}

data class MXSerieResponse(
    @SerializedName("flatrate") var flatrate: List<ItemMXSerieResponse>? = null
) {
    fun toDomain() = MXSerieModel(flatrate = toDomain(flatrate))
}

data class ItemMXSerieResponse(
    @SerializedName("logo_path") val logoPath: String? = null,
    @SerializedName("provider_name") val providerName: String? = null,
)

fun toDomain(list: List<ItemMXSerieResponse>?): List<ItemMXModel> {
    val listRet = mutableListOf<ItemMXModel>()
    list?.forEach {
        listRet.add(ItemMXModel(imageUrl = it.logoPath?.toImageURL(), name = it.providerName))
    }
    return listRet
}