package com.erwiin21mp.cinemovilplus.data

import com.erwiin21mp.cinemovilplus.data.network.api.APIService
import com.erwiin21mp.cinemovilplus.data.network.firebase.LogDataBaseManager
import com.erwiin21mp.cinemovilplus.domain.Repository
import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformsModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: APIService) : Repository {
    private companion object {
        const val API_KEY = "a91dbbaa0623f021f2c4220e3dd7a70a"
        const val LANGUAGE = "es-MX"
    }

    override suspend fun getDetailMovieById(id: String): ContentModel? {
        runCatching { apiService.getDetailsMovieById(id, API_KEY, LANGUAGE) }
            .onSuccess { return it.toDomain() }
            .onFailure { logErrorApi(id, it.message.orEmpty()) }
        return null
    }

    override suspend fun getDetailSerieById(id: String): ContentModel? {
        runCatching { apiService.getDetailsSerieById(id, API_KEY, LANGUAGE) }
            .onSuccess { return it.toDomain() }
            .onFailure { logErrorApi(id, it.message.orEmpty()) }
        return null
    }

    override suspend fun getWatchProvidersMovie(id: String): PlatformsModel? {
        runCatching { apiService.getPlatformsMovieById(id, API_KEY, LANGUAGE) }
            .onSuccess { return it.toDomain() }
            .onFailure { logErrorApi(id, it.message.orEmpty()) }
        return null
    }

    override suspend fun getWatchProvidersSerie(id: String): PlatformsModel? {
        runCatching { apiService.getPlatformsSerieById(id, API_KEY, LANGUAGE) }
            .onSuccess { return it.toDomain() }
            .onFailure { logErrorApi(id, it.message.orEmpty()) }
        return null
    }

    override suspend fun getCollectionDetails(id: String): CollectionModel? {
        runCatching { apiService.getCollectionDetails(id, API_KEY, LANGUAGE) }
            .onSuccess { return it.toDomain() }
            .onFailure { logErrorApi(id, it.message.orEmpty()) }
        return null
    }

    private fun logErrorApi(id: String, message: String) {
        LogDataBaseManager().logErrorApi(id, message)
    }
}