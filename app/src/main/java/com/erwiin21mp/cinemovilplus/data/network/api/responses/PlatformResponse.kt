package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.google.gson.annotations.SerializedName

data class PlatformResponse(
    @SerializedName("results") var results: ResultsResponse? = null
)

data class ResultsResponse(
    @SerializedName("MX") var mx: MxResponse? = null
)

data class MxResponse(
    @SerializedName("link") var link: String? = null,
    @SerializedName("flatrate") var flatrate: List<FlatrateResponse>? = null
)

data class FlatrateResponse(
    @SerializedName("logo_path") var imageURL: String? = null,
    @SerializedName("provider_name") var name: String? = null,
    @SerializedName("display_priority") var displayPriority: Int? = null
)