package com.erwiin21mp.cinemovilplus.data.model

import java.time.LocalDateTime

data class Content(
    val id: Int = 0,
    val idTmdb: Int = 0,
    val contentUrl: String = "",
    val isCameraQuality: Boolean = false,
    val isEnabled: Boolean = true,
    val keywords: String = "",
    val typeID: Int = 0,
    val uploadDate: LocalDateTime = LocalDateTime.now()
)