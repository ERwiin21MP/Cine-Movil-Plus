package com.erwiin21mp.cinemovilplus.domain.model

data class ContentModel(
    var horizontalImageURL: String? = null,
    var id: Int? = null,
    var releaseDate: String? = null,
    var title: String? = null,
    var verticalImageURL: String? = null,
    var collection: ItemCollectionModel? = null
)

data class ItemCollectionModel(
    var id: Int? = null,
    var name: String? = null,
    var horizontalImageURL: String? = null
)