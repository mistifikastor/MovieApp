// domain/repository/MovieRepository.kt
package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория фильмов
 */
interface MovieRepository {
    // Наблюдение за списком фильмов
    fun observeMovies(): Flow<List<Movie>>

    // Получение количества выбранных фильмов
    suspend fun getSelectedCount(): Int

    // Операции с фильмами
    suspend fun insertMovie(movie: Movie)
    suspend fun updateMovie(movie: Movie)
    suspend fun deleteMovie(movie: Movie)
    suspend fun deleteSelectedMovies()
    suspend fun clearAllSelections()

    // Поиск фильмов через API
    suspend fun searchMovies(query: String): List<Movie>
}