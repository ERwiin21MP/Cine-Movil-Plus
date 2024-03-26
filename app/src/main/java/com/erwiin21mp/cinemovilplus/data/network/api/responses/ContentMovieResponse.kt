package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.core.ext.toImageURL
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.Type.*
import com.google.gson.annotations.SerializedName

data class ContentMovieResponse(
    @SerializedName("backdrop_path") var horizontalImageURL: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("poster_path") var verticalImageURL: String? = null
) {
    fun toDomain() = ContentModel(
        horizontalImageURL = horizontalImageURL?.toImageURL(),
        id = id,
        releaseDate = releaseDate,
        title = title,
        verticalImageURL = verticalImageURL?.toImageURL(),
        type = Movie
    )
}