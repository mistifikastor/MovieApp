package com.example.movieapp.data.remote.model

import com.example.movieapp.domain.model.Movie

/**
 * Ответ от API при поиске фильмов
 */
data class MovieSearchResponse(
    val Search: List<MovieSearchResult>?,
    val totalResults: String?,
    val Response: String,
    val Error: String?
)

/**
 * Результат поиска фильма
 */
data class MovieSearchResult(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String
) {
    /**
     * Преобразование в доменную модель
     * (без жанра, так как API не возвращает жанр в поиске)
     */
    fun toDomain(): Movie = Movie(
        title = Title,
        year = Year,
        posterUrl = Poster,
        imdbID = imdbID,
        genre = null,
        isSelected = false
    )
}