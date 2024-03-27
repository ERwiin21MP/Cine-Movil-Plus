package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.core.ext.toImageURL
import com.erwiin21mp.cinemovilplus.domain.model.CompanyModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.Type.*
import com.google.gson.annotations.SerializedName

data class ContentMovieResponse(
    @SerializedName("backdrop_path") var horizontalImageURL: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("poster_path") var verticalImageURL: String? = null,
    @SerializedName("overview") var synopsis: String? = null,
    @SerializedName("production_countries") var productionCountries: List<CountryResponse>? = null,
    @SerializedName("original_title") var originalTitle: String? = null,
    @SerializedName("production_companies") var productionCompanies: List<CompanyResponse>? = null,
    @SerializedName("tagline") var tagline: String? = null
) {
    fun toDomain() = ContentModel(
        horizontalImageURL = horizontalImageURL?.toImageURL(),
        id = id,
        releaseDate = releaseDate,
        title = title,
        verticalImageURL = verticalImageURL?.toImageURL(),
        type = Movie,
        synopsis = synopsis,
        productionCountries = productionCountries?.toDomainCountry(),
        originalTitle = originalTitle,
        productionCompanies = productionCompanies?.toDomainCompany(),
        tagline = tagline
    )
}

data class CompanyResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("logo_path") var logo: String? = null,
    @SerializedName("name") var company: String? = null
)

data class CountryResponse(
    @SerializedName("name") var country: String? = null
)

fun List<CountryResponse>?.toDomainCountry() = this?.map { it.country.toString() }
fun List<CompanyResponse>?.toDomainCompany() =
    this?.map { CompanyModel(id = it.id, logo = it.logo?.toImageURL(), company = it.company) }