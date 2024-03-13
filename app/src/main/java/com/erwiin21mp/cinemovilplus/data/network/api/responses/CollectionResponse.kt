package com.erwiin21mp.cinemovilplus.data.network.api.responses

import com.erwiin21mp.cinemovilplus.core.ext.toImageURL
import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel
import com.google.gson.annotations.SerializedName

data class CollectionResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("backdrop_path") var horizontalImageURL: String? = null
) {
    fun toDomain() = CollectionModel(
        id = id,
        name = name,
        horizontalImageURL = horizontalImageURL?.toImageURL()
    )
}