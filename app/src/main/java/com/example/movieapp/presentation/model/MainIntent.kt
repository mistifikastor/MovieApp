package com.example.movieapp.presentation.model

import com.example.movieapp.domain.model.Movie

/**
 * Намерения пользователя (Intents) для главного экрана
 */
sealed class MainIntent {
    // Загрузка
    object LoadMovies : MainIntent()

    // Поиск
    data class SearchMovies(val query: String) : MainIntent()
    object ClearSearchResults : MainIntent()

    // Добавление/редактирование
    data class AddMovie(val movie: Movie) : MainIntent()
    object NavigateToAdd : MainIntent()
    data class NavigateToEdit(val movie: Movie) : MainIntent()

    // Выбор/удаление
    data class ToggleMovieSelection(val movie: Movie) : MainIntent()
    object DeleteSelectedMovies : MainIntent()
    object ShowDeleteDialog : MainIntent()
    object ConfirmDelete : MainIntent()
    object DismissDeleteDialog : MainIntent()

    // Навигация
    object NavigateBack : MainIntent()
    object NavigateToSearch : MainIntent()
}