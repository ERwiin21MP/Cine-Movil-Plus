package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.google.gson.annotations.SerializedName

data class ContentMovieSearchResponse(
    @SerializedName("title") var title: String? = null,
    @SerializedName("original_title") var originalTitle: String? = null,
    @SerializedName("overview") var synopsis: String? = null,
    @SerializedName("production_companies") var productionCompanies: List<CompanyResponse>,
    @SerializedName("production_countries") var productionCountries: List<CountryResponse>,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("poster_path") var verticalImageURL: String? = null,
    @SerializedName("tagline") var tagline: String? = null
)

data class CompanyResponse(
    @SerializedName("name") var company: String? = null
)

data class CountryResponse(
    @SerializedName("name") var country: String? = null
)