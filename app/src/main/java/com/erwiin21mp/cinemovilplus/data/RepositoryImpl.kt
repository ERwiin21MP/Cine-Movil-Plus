package com.erwiin21mp.cinemovilplus.data

import android.util.Log
import com.erwiin21mp.cinemovilplus.data.network.APIService
import com.erwiin21mp.cinemovilplus.domain.ContentModel
import com.erwiin21mp.cinemovilplus.domain.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: APIService) : Repository {
    override suspend fun getDetailMovie(
        id: String,
        apiKey: String,
        language: String
    ): ContentModel? {
        runCatching { apiService.getMovieDetail(id, apiKey, language) }
            .onSuccess { return it.toDomain() }
            .onFailure { Log.e("Erwin", "Error: ${it.message}") }
        return null
    }
}