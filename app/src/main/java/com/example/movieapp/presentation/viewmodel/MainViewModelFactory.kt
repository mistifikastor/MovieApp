package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.domain.usecase.movie.*
import com.example.movieapp.domain.usecase.search.SearchMoviesUseCase

class MainViewModelFactory(
    private val observeMoviesUseCase: ObserveMoviesUseCase,
    private val insertMovieUseCase: InsertMovieUseCase,
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val deleteSelectedMoviesUseCase: DeleteSelectedMoviesUseCase,
    private val toggleMovieSelectionUseCase: ToggleMovieSelectionUseCase,
    private val clearAllSelectionsUseCase: ClearAllSelectionsUseCase,
    private val getSelectedCountUseCase: GetSelectedCountUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                observeMoviesUseCase = observeMoviesUseCase,
                insertMovieUseCase = insertMovieUseCase,
                updateMovieUseCase = updateMovieUseCase,
                deleteMovieUseCase = deleteMovieUseCase,
                deleteSelectedMoviesUseCase = deleteSelectedMoviesUseCase,
                toggleMovieSelectionUseCase = toggleMovieSelectionUseCase,
                clearAllSelectionsUseCase = clearAllSelectionsUseCase,
                getSelectedCountUseCase = getSelectedCountUseCase,
                searchMoviesUseCase = searchMoviesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}