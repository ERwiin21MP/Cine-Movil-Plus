package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.core.ext.toImageURL
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.CreatedModel
import com.erwiin21mp.cinemovilplus.domain.model.NetworkModel
import com.erwiin21mp.cinemovilplus.domain.model.Type.*
import com.google.gson.annotations.SerializedName

data class ContentSerieResponse(
    @SerializedName("backdrop_path") var horizontalImageURL: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_air_date") var releaseDate: String? = null,
    @SerializedName("name") var title: String? = null,
    @SerializedName("poster_path") var verticalImageURL: String? = null,
    @SerializedName("overview") var synopsis: String? = null,
    @SerializedName("production_countries") var productionCountries: List<CountryResponse>? = null,
    @SerializedName("original_name") var originalTitle: String? = null,
    @SerializedName("production_companies") var productionCompanies: List<CompanyResponse>? = null,
    @SerializedName("created_by") var createdBy: List<CreatedResponse>? = null,
    @SerializedName("networks") var networks: List<NetworkResponse>? = null,
    @SerializedName("tagline") var tagline: String? = null
) {
    fun toDomain(): ContentModel {
        return ContentModel(
            horizontalImageURL = horizontalImageURL?.toImageURL(),
            id = id,
            releaseDate = releaseDate,
            title = title,
            verticalImageURL = verticalImageURL?.toImageURL(),
            type = Serie,
            synopsis = synopsis,
            productionCountries = productionCountries.toDomainCountry(),
            originalTitle = originalTitle,
            productionCompanies = productionCompanies?.toDomainCompany(),
            createdBy = createdBy.toDomainCreated(),
            networks = networks.toDomainNetwork(),
            tagline = tagline
        )
    }
}

data class CreatedResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("profile_path") var profilePath: String? = null
)

fun List<CreatedResponse>?.toDomainCreated() = this?.map {
    CreatedModel(
        id = it.id,
        name = it.name,
        profilePath = it.profilePath?.toImageURL()
    )
}

data class NetworkResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("logo_path") var logoPath: String? = null,
    @SerializedName("name") var name: String? = null
)

fun List<NetworkResponse>?.toDomainNetwork() =
    this?.map { NetworkModel(id = it.id, logoPath = it.logoPath?.toImageURL(), name = it.name) }