// data/repository/MovieRepositoryImpl.kt
package com.example.movieapp.data.repository

import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.local.model.MovieEntity
import com.example.movieapp.data.remote.api.MovieApi
import com.example.movieapp.data.remote.model.MovieSearchResult
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val movieApi: MovieApi,
    private val apiKey: String
) : MovieRepository {

    override fun observeMovies(): Flow<List<Movie>> =
        movieDao.getAllMovies().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getSelectedCount(): Int =
        movieDao.getSelectedCount()

    override suspend fun insertMovie(movie: Movie) {
        movieDao.insertMovie(MovieEntity.fromDomain(movie))
    }

    override suspend fun updateMovie(movie: Movie) {
        movieDao.updateMovie(MovieEntity.fromDomain(movie))
    }

    override suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(MovieEntity.fromDomain(movie))
    }

    override suspend fun deleteSelectedMovies() {
        movieDao.deleteSelectedMovies()
    }

    override suspend fun clearAllSelections() {
        movieDao.clearAllSelections()
    }

    override suspend fun searchMovies(query: String): List<Movie> =
        withContext(Dispatchers.IO) {
            try {
                val response = movieApi.searchMovies(apiKey, query)
                if (response.Response == "True" && response.Search != null) {
                    response.Search.mapIndexed { index, result ->
                        // Временная генерация жанра
                        val genres = listOf("Драма", "Комедия", "Боевик", "Триллер",
                            "Фантастика", "Ужасы", "Мелодрама", "Детектив")
                        result.toDomain().copy(
                            genre = genres[index % genres.size]
                        )
                    }
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
}