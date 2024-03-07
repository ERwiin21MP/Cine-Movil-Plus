package com.erwiin21mp.cinemovilplus.data

import android.util.Log
import com.erwiin21mp.cinemovilplus.data.network.APIService
import com.erwiin21mp.cinemovilplus.domain.ContentModel
import com.erwiin21mp.cinemovilplus.domain.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: APIService) : Repository {
    companion object {
        const val API_KEY = "a91dbbaa0623f021f2c4220e3dd7a70a"
        const val LANGUAGE = "es-MX"
    }
    override suspend fun getDetailMovie(id: String): ContentModel? {
        runCatching { apiService.getMovieDetail(id, API_KEY, LANGUAGE) }
            .onSuccess { return it.toDomain() }
            .onFailure { Log.e("Erwin", "Error: ${it.message}") }
        return null
    }
}