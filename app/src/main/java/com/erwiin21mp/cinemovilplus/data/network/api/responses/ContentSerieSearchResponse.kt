package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.core.ext.toImageURL
import com.erwiin21mp.cinemovilplus.domain.model.CompanyModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentSerieSearchModel
import com.erwiin21mp.cinemovilplus.domain.model.CountryModel
import com.erwiin21mp.cinemovilplus.domain.model.CreatedModel
import com.erwiin21mp.cinemovilplus.domain.model.NetworkModel
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
) {
    fun toDomain() = ContentSerieSearchModel(
        title = title,
        originalTitle = originalTitle,
        synopsis = synopsis,
        productionCompanies = productionCompanies.distinct()
            .joinToString(separator = ", ") { it.company.toString() },
        productionCountries = productionCountries.distinct()
            .joinToString(separator = ", ") { it.country.toString() },
        releaseDate = releaseDate,
        verticalImageURL = verticalImageURL?.toImageURL(),
        createdBy = createdBy.toDomainCreated(),
        networks = networks.toDomainNetwork(),
        tagline = tagline,
        country = country
    )
}

fun List<CompanyResponse>?.toDomainCompany() =
    this?.map { CompanyModel(company = it.company) } ?: emptyList()

fun List<CountryResponse>?.toDomainCountry() =
    this?.map { CountryModel(country = it.country) } ?: emptyList()

fun List<CreatedResponse>?.toDomainCreated() =
    this?.map { CreatedModel(name = it.name) } ?: emptyList()

fun List<NetworkResponse>?.toDomainNetwork() =
    this?.map { NetworkModel(name = it.name) } ?: emptyList()

data class CreatedResponse(
    @SerializedName("name") var name: String? = null
)

data class NetworkResponse(
    @SerializedName("name") var name: String? = null
)