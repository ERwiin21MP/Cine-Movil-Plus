package com.erwiin21mp.cinemovilplus.domain.model

data class ContentHomeModel(
    var id: String? = null,
    var idTmdb: String? = null,
    var isCameraQuality: Boolean? = null,
    var type: String? = null,
    var uploadDate: Long? = null,
    var horizontalImageURL: String? = null,
    var releaseDate: Long? = null,
    var title: String? = null,
    var verticalImageURL: String? = null,
    var collection: ItemCollectionModel? = null
)
