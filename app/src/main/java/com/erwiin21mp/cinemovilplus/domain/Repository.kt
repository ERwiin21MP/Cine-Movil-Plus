package com.erwiin21mp.cinemovilplus.domain

import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformMovieModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformSerieModel

interface Repository {
    suspend fun getDetailMovieById(id: String): ContentModel?
    suspend fun getDetailSerieById(id: String): ContentModel?
    suspend fun getWatchProvidersMovie(id: String): PlatformMovieModel?
    suspend fun getWatchProvidersSerie(id: String): PlatformSerieModel?
}