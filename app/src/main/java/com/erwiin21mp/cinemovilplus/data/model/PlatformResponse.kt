package com.erwiin21mp.cinemovilplus.data.model

import com.erwiin21mp.cinemovilplus.domain.model.ItemMXModel
import com.erwiin21mp.cinemovilplus.domain.model.MXModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformModel
import com.erwiin21mp.cinemovilplus.domain.model.ResultsModel
import com.google.gson.annotations.SerializedName

data class PlatformResponse(
    @SerializedName("results") var results: ResultsResponse
) {
    fun toDomain() = PlatformModel(results = results.toDomain())
}

data class ResultsResponse(
    @SerializedName("MX") var mx: MXResponse
) {
    fun toDomain() = ResultsModel(mx = mx.toDomain())
}

data class MXResponse(
    @SerializedName("rent") var rent: List<ItemMXResponse>?,
    @SerializedName("buy") var buy: List<ItemMXResponse>?,
    @SerializedName("flatrate") var flatrate: List<ItemMXResponse>?
) {
    fun toDomain() = MXModel(
        rent = toDomain(rent),
        buy = toDomain(buy),
        flatrate = toDomain(flatrate)
    )
}

data class ItemMXResponse(
    @SerializedName("logo_path") val logoPath: String,
    @SerializedName("provider_name") val providerName: String,
)

fun toDomain(list: List<ItemMXResponse>?): List<ItemMXModel> {
    val listRet = mutableListOf<ItemMXModel>()
    list?.forEach {
        listRet.add(ItemMXModel(logoPath = it.logoPath, providerName = it.providerName))
    }
    return listRet
}