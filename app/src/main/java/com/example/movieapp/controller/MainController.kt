// app/src/main/java/com/example/movieapp/controller/MainController.kt
package com.example.movieapp.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.model.Movie
import com.example.movieapp.model.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainController(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    // Меняем тип на List<Movie>
    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _selectedCount = MutableStateFlow(0)
    val selectedCount: StateFlow<Int> = _selectedCount.asStateFlow()

    init {
        loadMoviesFromDb()
    }

    fun loadMoviesFromDb() {
        viewModelScope.launch {
            repository.allMovies.collect { moviesList ->
                _movies.value = moviesList
                _selectedCount.value = moviesList.count { it.isSelected }
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val results = repository.searchMovies(query)
                _searchResults.value = results
                if (results.isEmpty()) {
                    _errorMessage.value = "Фильмы не найдены"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            repository.insertMovie(movie)
            loadMoviesFromDb()
        }
    }

    fun toggleMovieSelection(movie: Movie) {
        viewModelScope.launch {
            repository.updateMovie(movie.copy(isSelected = !movie.isSelected))
            loadMoviesFromDb()
        }
    }

    fun deleteSelectedMovies() {
        viewModelScope.launch {
            repository.deleteSelectedMovies()
            loadMoviesFromDb()
        }
    }

    fun clearAllSelections() {
        viewModelScope.launch {
            repository.clearAllSelections()
            loadMoviesFromDb()
        }
    }

    fun clearSearchResults() {
        _searchResults.value = emptyList()
        _errorMessage.value = null
    }
}