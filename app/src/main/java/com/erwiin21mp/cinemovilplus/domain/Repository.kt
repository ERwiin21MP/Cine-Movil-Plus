package com.erwiin21mp.cinemovilplus.domain

interface Repository {
    suspend fun getDetailMovie(id: String, apiKey: String, language: String): ContentModel?
}