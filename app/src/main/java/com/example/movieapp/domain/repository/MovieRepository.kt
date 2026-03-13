package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для работы с фильмами
 * Определяет контракт между domain и data слоями
 */
interface MovieRepository {
    // Наблюдение за списком фильмов (Room)
    fun observeMovies(): Flow<List<Movie>>

    // CRUD операции
    suspend fun insertMovie(movie: Movie)
    suspend fun updateMovie(movie: Movie)
    suspend fun deleteMovie(movie: Movie)
    suspend fun deleteSelectedMovies()
    suspend fun clearAllSelections()

    // Подсчет выбранных фильмов
    suspend fun getSelectedCount(): Int

    // Поиск фильмов через API
    suspend fun searchMovies(query: String): List<Movie>
}