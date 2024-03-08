package com.erwiin21mp.cinemovilplus.data

import com.erwiin21mp.cinemovilplus.data.network.APIService
import com.erwiin21mp.cinemovilplus.data.network.firebase.DataBaseManager
import com.erwiin21mp.cinemovilplus.domain.Repository
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: APIService) : Repository {
    companion object {
        const val API_KEY = "a91dbbaa0623f021f2c4220e3dd7a70a"
        const val LANGUAGE = "es-MX"
    }

    override suspend fun getDetailMovieById(id: String): ContentModel? {
        runCatching { apiService.getMovieDetailById(id, API_KEY, LANGUAGE) }
            .onSuccess { return it.toDomain() }
            .onFailure { logErrorApi(id, it.message.orEmpty()) }
        return null
    }

    override suspend fun getDetailSerieById(id: String): ContentModel? {
        runCatching { apiService.getSerieDetailById(id, API_KEY, LANGUAGE) }
            .onSuccess { return it.toDomain() }
            .onFailure { logErrorApi(id, it.message.orEmpty()) }
        return null
    }

    override suspend fun getWatchProvidersMovie(id: String): PlatformModel? {
        runCatching { apiService.getMovieWatchProvidersById(id, API_KEY, LANGUAGE) }
            .onSuccess { return it.toDomain() }
            .onFailure { logErrorApi(id, it.message.orEmpty()) }
        return null
    }

    private fun logErrorApi(id: String, message: String) {
        DataBaseManager().logErrorApi(id, message)
    }
}