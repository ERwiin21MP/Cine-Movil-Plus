package com.erwiin21mp.cinemovilplus.data.network.api

import com.erwiin21mp.cinemovilplus.data.network.api.responses.CollectionResponse
import com.erwiin21mp.cinemovilplus.data.network.api.responses.ContentMovieResponse
import com.erwiin21mp.cinemovilplus.data.network.api.responses.ContentSerieResponse
import com.erwiin21mp.cinemovilplus.data.network.api.responses.PlatformsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("movie/{movie_id}")
    suspend fun getDetailsMovieById(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): ContentMovieResponse

    @GET("tv/{series_id}")
    suspend fun getDetailsSerieById(
        @Path("series_id") serieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): ContentSerieResponse

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getWatchProvidersMovieById(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): PlatformsResponse

    @GET("tv/{series_id}/watch/providers")
    suspend fun getWatchProvidersSerieById(
        @Path("series_id") seriesId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): PlatformsResponse

    @GET("collection/{collection_id}")
    suspend fun getCollectionDetails(
        @Path("collection_id") collectionId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): CollectionResponse
}