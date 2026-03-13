package com.example.movieapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.domain.model.Movie

/**
 * Entity для Room базы данных
 * Представляет таблицу movies в БД
 */
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
    /**
     * Преобразование Entity в доменную модель
     */
    fun toDomain(): Movie = Movie(
        id = id,
        title = title,
        year = year,
        posterUrl = posterUrl,
        imdbID = imdbID,
        genre = genre,
        isSelected = isSelected
    )

    companion object {
        /**
         * Преобразование доменной модели в Entity
         */
        fun fromDomain(movie: Movie): MovieEntity = MovieEntity(
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