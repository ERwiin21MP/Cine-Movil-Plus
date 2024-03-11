package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.domain.model.ItemMXModel
import com.erwiin21mp.cinemovilplus.domain.model.MXMovieModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformMovieModel
import com.erwiin21mp.cinemovilplus.domain.model.ResultsModel
import com.google.gson.annotations.SerializedName

data class PlatformMovieResponse(
    @SerializedName("results") var results: ResultsMovieResponse? = null
) {
    fun toDomain() = PlatformMovieModel(results = results?.toDomain())
}

data class ResultsMovieResponse(
    @SerializedName("MX") var mx: MXMovieResponse? = null
) {
    fun toDomain() = ResultsModel(mx = mx?.toDomain())
}

data class MXMovieResponse(
    @SerializedName("rent") var rent: List<ItemMXMovieResponse>? = null,
    @SerializedName("buy") var buy: List<ItemMXMovieResponse>? = null,
    @SerializedName("flatrate") var flatrate: List<ItemMXMovieResponse>? = null
) {
    fun toDomain() = MXMovieModel(
        rent = toDomain(rent),
        buy = toDomain(buy),
        flatrate = toDomain(flatrate)
    )
}

data class ItemMXMovieResponse(
    @SerializedName("logo_path") val logoPath: String? = null,
    @SerializedName("provider_name") val providerName: String? = null,
)

fun toDomain(list: List<ItemMXMovieResponse>?): List<ItemMXModel> {
    val listRet = mutableListOf<ItemMXModel>()
    list?.forEach {
        listRet.add(ItemMXModel(imageUrl = it.logoPath, name = it.providerName))
    }
    return listRet
}