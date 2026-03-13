// data/remote/model/MovieSearchResponse.kt (бывший MovieResponse.kt)
package com.example.movieapp.data.remote.model

data class MovieSearchResponse(
    val Search: List<MovieSearchResult>?,
    val totalResults: String?,
    val Response: String,
    val Error: String?
)

data class MovieSearchResult(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String
) {
    fun toDomain() = com.example.movieapp.domain.model.Movie(
        title = Title,
        year = Year,
        posterUrl = Poster,
        imdbID = imdbID,
        genre = null, // Жанр будет получен отдельно
        isSelected = false
    )
}