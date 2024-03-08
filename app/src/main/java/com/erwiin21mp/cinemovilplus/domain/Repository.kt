package com.erwiin21mp.cinemovilplus.domain

import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformModel

interface Repository {
    suspend fun getDetailMovieById(id: String): ContentModel?
    suspend fun getDetailSerieById(id: String): ContentModel?
    suspend fun getWatchProvidersMovie(id: String): PlatformModel?
}