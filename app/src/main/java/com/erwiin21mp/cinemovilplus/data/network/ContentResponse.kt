package com.erwiin21mp.cinemovilplus.data.network

import com.erwiin21mp.cinemovilplus.domain.ContentModel
import com.google.gson.annotations.SerializedName

data class ContentResponse(
    @SerializedName("backdrop_path") var horizontalImageURL: String,
    @SerializedName("id") var id: Int,
    @SerializedName("release_date") var releaseDate: String,
    @SerializedName("title") var title: String,
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