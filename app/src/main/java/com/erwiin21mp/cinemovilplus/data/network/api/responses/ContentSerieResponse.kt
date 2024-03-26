package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.core.ext.toImageURL
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.Type.*
import com.google.gson.annotations.SerializedName

data class ContentSerieResponse(
    @SerializedName("backdrop_path") var horizontalImageURL: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_air_date") var releaseDate: String? = null,
    @SerializedName("name") var title: String? = null,
    @SerializedName("poster_path") var verticalImageURL: String? = null
) {
    fun toDomain(): ContentModel {
        return ContentModel(
            horizontalImageURL = horizontalImageURL?.toImageURL(),
            id = id,
            releaseDate = releaseDate,
            title = title,
            verticalImageURL = verticalImageURL?.toImageURL(),
            type = Serie
        )
    }
}