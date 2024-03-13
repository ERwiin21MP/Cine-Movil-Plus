package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.google.gson.annotations.SerializedName

data class ContentSerieResponse(
    @SerializedName("backdrop_path") var horizontalImageURL: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_air_date") var releaseDate: String? = null,
    @SerializedName("name") var title: String? = null,
    @SerializedName("poster_path") var verticalImageURL: String? = null,
    @SerializedName("belongs_to_collection") var saga: List<SagaItemsResponse>? = null
) {
    fun toDomain(): ContentModel {
        return ContentModel(
            horizontalImageURL = horizontalImageURL,
            id = id,
            releaseDate = releaseDate,
            title = title,
            verticalImageURL = verticalImageURL
        )
    }
}