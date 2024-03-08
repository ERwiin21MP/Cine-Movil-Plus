package com.erwiin21mp.cinemovilplus.data.network

import com.erwiin21mp.cinemovilplus.data.model.ContentMovieResponse
import com.erwiin21mp.cinemovilplus.data.model.ContentSerieResponse
import com.erwiin21mp.cinemovilplus.data.model.PlatformResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("movie/{movie_id}")
    suspend fun getMovieDetailById(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): ContentMovieResponse

    @GET("tv/{series_id}")
    suspend fun getSerieDetailById(
        @Path("series_id") serieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): ContentSerieResponse

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieWatchProvidersById(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): PlatformResponse
}