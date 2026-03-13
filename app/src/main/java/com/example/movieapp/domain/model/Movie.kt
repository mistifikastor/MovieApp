// domain/model/Movie.kt
package com.example.movieapp.domain.model

data class Movie(
    val id: Int = 0,
    val title: String,
    val year: String,
    val posterUrl: String,
    val imdbID: String,
    val genre: String? = null,
    val isSelected: Boolean = false
)