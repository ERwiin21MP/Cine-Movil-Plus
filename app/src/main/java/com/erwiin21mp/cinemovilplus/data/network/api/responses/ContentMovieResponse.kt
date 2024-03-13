package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.SagaItemsModel
import com.google.gson.annotations.SerializedName

data class ContentMovieResponse(
    @SerializedName("backdrop_path") var horizontalImageURL: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("poster_path") var verticalImageURL: String? = null,
    @SerializedName("belongs_to_collection") var saga: List<SagaItemsResponse>? = null
) {
    fun toDomain() = ContentModel(
        horizontalImageURL = horizontalImageURL,
        id = id,
        releaseDate = releaseDate,
        title = title,
        verticalImageURL = verticalImageURL,
        saga = toDomain(saga)
    )

    private fun toDomain(saga: List<SagaItemsResponse>?): List<SagaItemsModel>? {
        val listRet = mutableListOf<SagaItemsModel>()
        saga?.forEach {
            listRet.add(
                SagaItemsModel(
                    id = it.id,
                    name = it.name,
                    horizontalImageURL = it.horizontalImageURL
                )
            )
        }
        return listRet
    }
}

data class SagaItemsResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("backdrop_path") var horizontalImageURL: String? = null
)