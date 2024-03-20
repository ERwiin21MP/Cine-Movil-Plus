package com.erwiin21mp.cinemovilplus.data.network.api.responses


import com.erwiin21mp.cinemovilplus.core.ext.toImageURL
import com.erwiin21mp.cinemovilplus.domain.model.ContentMovieSearchModel
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
) {
    fun toDomain() = ContentMovieSearchModel(
        title = title,
        originalTitle = originalTitle,
        synopsis = synopsis,
        productionCompanies = productionCompanies.toDomainCompany(),
        productionCountries = productionCountries.toDomainCountry(),
        releaseDate = releaseDate,
        verticalImageURL = verticalImageURL?.toImageURL(),
        tagline = tagline
    )
}

data class CompanyResponse(
    @SerializedName("name") var company: String? = null
)

data class CountryResponse(
    @SerializedName("name") var country: String? = null
)