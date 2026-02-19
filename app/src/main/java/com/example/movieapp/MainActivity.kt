// app/src/main/java/com/example/movieapp/MainActivity.kt
package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.movieapp.controller.MainController
import com.example.movieapp.model.Movie
import com.example.movieapp.model.MovieRepository
import com.example.movieapp.view.AddScreen
import com.example.movieapp.view.MainScreen
import com.example.movieapp.view.MovieView
import com.example.movieapp.view.SearchScreen

class MainActivity : ComponentActivity(), MovieView {

    // Состояние для UI (будет обновляться контроллером)
    private var movies by mutableStateOf(emptyList<Movie>())
    private var searchResults by mutableStateOf(emptyList<Movie>())
    private var isLoading by mutableStateOf(false)
    private var errorMessage by mutableStateOf<String?>(null)
    private var selectedCount by mutableStateOf(0)

    // Навигация
    private var currentScreen by mutableStateOf(Screen.MAIN)
    private var selectedMovieForEdit by mutableStateOf<Movie?>(null)

    // Контроллер
    private lateinit var controller: MainController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализируем контроллер
        val repository = MovieRepository(applicationContext)
        controller = MainController(repository, this)

        setContent {
            MaterialTheme {
                // Просто отображаем текущий экран с текущим состоянием
                when (currentScreen) {
                    Screen.MAIN -> MainScreen(
                        movies = movies,
                        selectedCount = selectedCount,
                        onAddClick = { controller.onAddClicked() },
                        onMovieToggle = { controller.toggleMovieSelection(it) },
                        onDeleteSelected = { controller.deleteSelectedMovies() }
                    )

                    Screen.ADD -> AddScreen(
                        onBack = { controller.onBackClicked() },
                        onOpenSearch = { controller.onSearchClicked() },
                        onAddMovie = { controller.addMovie(it) },
                        selectedMovie = selectedMovieForEdit
                    )

                    Screen.SEARCH -> SearchScreen(
                        searchResults = searchResults,
                        isLoading = isLoading,
                        errorMessage = errorMessage,
                        onSearch = { controller.searchMovies(it) },
                        onBack = { controller.onBackClicked() },
                        onMovieSelected = { movie ->
                            controller.onMovieSelectedForEdit(movie)
                        },
                        onClearResults = { controller.clearSearchResults() }
                    )
                }
            }
        }

        // Загружаем данные
        controller.loadMoviesFromDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.onDestroy()
    }

    // Реализация MovieView
    override fun showMovies(movies: List<Movie>) {
        this.movies = movies
    }

    override fun showSearchResults(results: List<Movie>) {
        this.searchResults = results
    }

    override fun showLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    override fun showError(message: String?) {
        this.errorMessage = message
    }

    override fun updateSelectedCount(count: Int) {
        this.selectedCount = count
    }

    override fun navigateToAdd(movie: Movie?) {
        selectedMovieForEdit = movie
        currentScreen = Screen.ADD
    }

    override fun navigateToSearch() {
        currentScreen = Screen.SEARCH
    }

    override fun navigateBack() {
        when (currentScreen) {
            Screen.SEARCH -> currentScreen = Screen.ADD
            else -> currentScreen = Screen.MAIN
        }
    }
}

enum class Screen {
    MAIN, ADD, SEARCH
}