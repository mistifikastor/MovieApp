// data/local/model/MovieEntity.kt (бывший Movie.kt)
package com.example.movieapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val year: String,
    val posterUrl: String,
    val imdbID: String,
    var genre: String? = null,
    var isSelected: Boolean = false
) {
    // Маппинг в доменную модель
    fun toDomain() = com.example.movieapp.domain.model.Movie(
        id = id,
        title = title,
        year = year,
        posterUrl = posterUrl,
        imdbID = imdbID,
        genre = genre,
        isSelected = isSelected
    )

    companion object {
        fun fromDomain(movie: com.example.movieapp.domain.model.Movie) = MovieEntity(
            id = movie.id,
            title = movie.title,
            year = movie.year,
            posterUrl = movie.posterUrl,
            imdbID = movie.imdbID,
            genre = movie.genre,
            isSelected = movie.isSelected
        )
    }
}