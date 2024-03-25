package com.erwiin21mp.cinemovilplus.ui.view.home

import com.erwiin21mp.cinemovilplus.domain.model.ContentModel

sealed class ContentFeaturedState {
    data object Loading : ContentFeaturedState()
    data class Error(val error: String) : ContentFeaturedState()
    data class Success(val list: List<ContentModel>) : ContentFeaturedState()
}
