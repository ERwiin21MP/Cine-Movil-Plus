package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.google.gson.annotations.SerializedName

data class ContentSerieSearchResponse(
    @SerializedName("name") var title: String? = null,
    @SerializedName("original_name") var originalTitle: String? = null,
    @SerializedName("overview") var synopsis: String? = null,
    @SerializedName("production_companies") var productionCompanies: List<CompanyResponse>,
    @SerializedName("production_countries") var productionCountries: List<CountryResponse>,
    @SerializedName("first_air_date") var releaseDate: String? = null,
    @SerializedName("poster_path") var verticalImageURL: String? = null,
    @SerializedName("created_by") var createdBy: List<CreatedResponse>,
    @SerializedName("networks") var networks: List<NetworkResponse>,
    @SerializedName("tagline") var tagline: String? = null,
    @SerializedName("origin_country") var country: List<String>
)

data class CreatedResponse(
    @SerializedName("name") var name: String? = null
)

data class NetworkResponse(
    @SerializedName("name") var name: String? = null
)