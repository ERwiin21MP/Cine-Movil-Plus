package com.erwiin21mp.cinemovilplus.data.network.retrofit.content

import com.google.gson.annotations.SerializedName

data class ContentResponse(
    @SerializedName("backdrop_path") var horizontalImageURL: String,
    @SerializedName("belongs_to_collection") var collections: List<CollectionsItems>,
    @SerializedName("id") var id: Int,
    @SerializedName("release_date") var releaseDate: String,
    @SerializedName("title") var title: String,
    @SerializedName("poster_path") var verticalImageURL: String
)

data class CollectionsItems(@SerializedName("id") var id: Int)