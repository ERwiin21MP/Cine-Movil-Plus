package com.erwiin21mp.cinemovilplus.data.homeProviders

import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import javax.inject.Inject

class ContentFeaturedProvider @Inject constructor() {
    fun getContentFeatured(contentList: List<ContentInitModel>): List<ContentInitModel> {
        return contentList
    }
}