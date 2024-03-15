package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.core.ext.toImageURL
import com.erwiin21mp.cinemovilplus.domain.model.FlatrateModel
import com.erwiin21mp.cinemovilplus.domain.model.MxModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformsModel
import com.erwiin21mp.cinemovilplus.domain.model.ResultsModel
import com.google.gson.annotations.SerializedName

data class PlatformsResponse(
    @SerializedName("results") var results: ResultsResponse? = null
) {
    fun toDomain() = PlatformsModel(
        results = results?.toDomain()
    )
}

data class ResultsResponse(
    @SerializedName("MX") var mx: MxResponse? = null
) {
    fun toDomain() = ResultsModel(
        mx = mx?.toDomain()
    )
}

data class MxResponse(
    @SerializedName("flatrate") var flatrate: List<FlatrateResponse>? = null
) {
    fun toDomain() = MxModel(
        flatrate = flatrate.toDomain()
    )
}

fun List<FlatrateResponse>?.toDomain(): List<FlatrateModel> {
    val listRet = mutableListOf<FlatrateModel>()
    this?.forEach {
        listRet.add(
            FlatrateModel(
                imageURL = it.imageURL?.toImageURL(),
                name = it.name,
                displayPriority = it.displayPriority
            )
        )
    }
    return listRet
}

data class FlatrateResponse(
    @SerializedName("logo_path") var imageURL: String? = null,
    @SerializedName("provider_name") var name: String? = null,
    @SerializedName("display_priority") var displayPriority: Int? = null
)