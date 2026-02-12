// app/src/main/java/com/example/movieapp/model/MovieRepository.kt
package com.example.movieapp.model

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(context: Context) {
    private val movieDao = MovieDatabase.getDatabase(context).movieDao()

    val allMovies: Flow<List<Movie>> = movieDao.getAllMovies()

    suspend fun insertMovie(movie: Movie) {
        movieDao.insertMovie(movie.copy(isSelected = false))
    }

    suspend fun updateMovie(movie: Movie) {
        movieDao.updateMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie)
    }

    suspend fun deleteSelectedMovies() {
        movieDao.deleteSelectedMovies()
    }

    suspend fun clearAllSelections() {
        movieDao.clearAllSelections()
    }

    suspend fun getSelectedCount(): Int {
        return movieDao.getSelectedCount()
    }

    suspend fun searchMovies(query: String): List<Movie> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.instance.searchMovies(RetrofitClient.API_KEY, query)
            if (response.Response == "True" && response.Search != null) {
                response.Search.map { result ->
                    Movie(
                        title = result.Title,
                        year = result.Year,
                        posterUrl = result.Poster,
                        imdbID = result.imdbID,
                        isSelected = false
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