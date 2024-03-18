package com.erwiin21mp.cinemovilplus.domain.model

sealed class Type {
    data object Movie : Type()
    data object Serie : Type()
}
