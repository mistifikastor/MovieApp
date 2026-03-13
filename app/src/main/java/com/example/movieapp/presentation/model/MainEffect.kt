package com.example.movieapp.presentation.model

import com.example.movieapp.domain.model.Movie

/**
 * Одноразовые события (эффекты) для главного экрана
 */
sealed class MainEffect {
    object NavigateToMain : MainEffect()
    data class NavigateToAdd(val movie: Movie?) : MainEffect()
    object NavigateToSearch : MainEffect()
    object NavigateBack : MainEffect()
    data class ShowError(val message: String) : MainEffect()
}