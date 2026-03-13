package com.example.movieapp.data.remote.api

import com.example.movieapp.data.remote.model.MovieDetailsResponse
import com.example.movieapp.data.remote.model.MovieSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Интерфейс для работы с OMDB API
 */
interface MovieApi {
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") searchQuery: String,
        @Query("type") type: String = "movie"
    ): MovieSearchResponse

    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbID: String,
        @Query("plot") plot: String = "short"
    ): MovieDetailsResponse
}