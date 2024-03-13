package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.core.ext.toURLImage
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.ItemCollectionModel
import com.google.gson.annotations.SerializedName

data class ContentMovieResponse(
    @SerializedName("backdrop_path") var horizontalImageURL: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("poster_path") var verticalImageURL: String? = null,
    @SerializedName("belongs_to_collection") var collection: ItemCollectionResponse? = null
) {
    fun toDomain() = ContentModel(
        horizontalImageURL = horizontalImageURL,
        id = id,
        releaseDate = releaseDate,
        title = title,
        verticalImageURL = verticalImageURL,
        collection = collection?.toDomain()
    )
}

data class ItemCollectionResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("backdrop_path") var horizontalImageURL: String? = null
) {
    fun toDomain() = ItemCollectionModel(
        id = id,
        name = name,
        horizontalImageURL = horizontalImageURL?.toURLImage()
    )
}