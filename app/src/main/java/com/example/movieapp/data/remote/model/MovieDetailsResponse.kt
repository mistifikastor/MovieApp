package com.example.movieapp.data.remote.model

import com.example.movieapp.domain.model.Movie

/**
 * Детальный ответ от API о фильме
 */
data class MovieDetailsResponse(
    val Title: String,
    val Year: String,
    val Rated: String,
    val Released: String,
    val Runtime: String,
    val Genre: String,
    val Director: String,
    val Writer: String,
    val Actors: String,
    val Plot: String,
    val Language: String,
    val Country: String,
    val Awards: String,
    val Poster: String,
    val imdbID: String,
    val Response: String,
    val Error: String?
) {
    /**
     * Преобразование в доменную модель
     */
    fun toDomain(): Movie = Movie(
        title = Title,
        year = Year,
        posterUrl = Poster,
        imdbID = imdbID,
        genre = Genre,
        isSelected = false
    )
}