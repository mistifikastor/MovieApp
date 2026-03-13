package com.example.movieapp.presentation.model

import com.example.movieapp.domain.model.Movie

/**
 * Состояние главного экрана
 */
data class MainState(
    val movies: List<Movie> = emptyList(),
    val searchResults: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedCount: Int = 0,
    val isDeleteDialogVisible: Boolean = false,
    val currentScreen: Screen = Screen.MAIN,
    val selectedMovieForEdit: Movie? = null
)

/**
 * Возможные экраны приложения
 */
enum class Screen {
    MAIN, ADD, SEARCH
}