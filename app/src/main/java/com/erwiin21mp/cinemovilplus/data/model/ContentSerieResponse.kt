package com.erwiin21mp.cinemovilplus.data.model

import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.google.gson.annotations.SerializedName

data class ContentSerieResponse(
    @SerializedName("backdrop_path") var horizontalImageURL: String,
    @SerializedName("id") var id: Int,
    @SerializedName("first_air_date") var releaseDate: String,
    @SerializedName("name") var title: String,
    @SerializedName("poster_path") var verticalImageURL: String
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