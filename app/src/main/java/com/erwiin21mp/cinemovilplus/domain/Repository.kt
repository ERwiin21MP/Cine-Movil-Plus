package com.erwiin21mp.cinemovilplus.domain

import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentMovieSearchModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentSerieSearchModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformsModel

interface Repository {
    suspend fun getDetailMovieById(id: String): ContentModel?
    suspend fun getDetailSerieById(id: String): ContentModel?
    suspend fun getWatchProvidersMovie(id: String): PlatformsModel?
    suspend fun getWatchProvidersSerie(id: String): PlatformsModel?
    suspend fun getCollectionDetails(id: String): CollectionModel?
    suspend fun getMovieSearch(id: String): ContentMovieSearchModel?
    suspend fun getSerieSearch(id: String): ContentSerieSearchModel?
}