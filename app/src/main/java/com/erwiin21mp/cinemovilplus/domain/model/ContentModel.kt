package com.erwiin21mp.cinemovilplus.domain.model

data class ContentModel(
    var horizontalImageURL: String? = null,
    var id: Int? = null,
    var releaseDate: String? = null,
    var title: String? = null,
    var verticalImageURL: String? = null,
    var saga: List<SagaItemsModel>? = null
)

data class SagaItemsModel(
    var id: Int? = null,
    var name: String? = null,
    var horizontalImageURL: String? = null
)