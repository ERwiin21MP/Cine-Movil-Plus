package com.erwiin21mp.cinemovilplus.domain.model

data class ContentHomeModel(
    var id: String,
    var idTmdb: String,
    var isCameraQuality: Boolean,
    var isEnabled: Boolean,
    var typeID: Int,
    var uploadDate: Long,
    var horizontalImageURL: String = "",
    var releaseDate: String = "",
    var title: String = "",
    var verticalImageURL: String = ""
)
