// presentation/viewmodel/MainViewModel.kt
package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.usecase.movie.*
import com.example.movieapp.domain.usecase.search.SearchMoviesUseCase
import com.example.movieapp.presentation.model.MainEffect
import com.example.movieapp.presentation.model.MainIntent
import com.example.movieapp.presentation.model.MainState
import com.example.movieapp.presentation.model.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val observeMoviesUseCase: ObserveMoviesUseCase,
    private val insertMovieUseCase: InsertMovieUseCase,
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val deleteSelectedMoviesUseCase: DeleteSelectedMoviesUseCase,
    private val toggleMovieSelectionUseCase: ToggleMovieSelectionUseCase,
    private val clearAllSelectionsUseCase: ClearAllSelectionsUseCase,
    private val getSelectedCountUseCase: GetSelectedCountUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect: SharedFlow<MainEffect> = _effect.asSharedFlow()

    init {
        observeMovies()
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadMovies -> observeMovies()
            is MainIntent.SearchMovies -> searchMovies(intent.query)
            is MainIntent.ClearSearchResults -> clearSearchResults()
            is MainIntent.AddMovie -> addMovie(intent.movie)
            is MainIntent.NavigateToAdd -> navigateToAdd()
            is MainIntent.NavigateToEdit -> navigateToEdit(intent.movie)
            is MainIntent.ToggleMovieSelection -> toggleMovieSelection(intent.movie)
            is MainIntent.DeleteSelectedMovies -> deleteSelectedMovies()
            is MainIntent.ShowDeleteDialog -> showDeleteDialog()
            is MainIntent.ConfirmDelete -> confirmDelete()
            is MainIntent.DismissDeleteDialog -> dismissDeleteDialog()
            is MainIntent.NavigateBack -> navigateBack()
            is MainIntent.NavigateToSearch -> navigateToSearch()
        }
    }

    private fun observeMovies() {
        viewModelScope.launch {
            observeMoviesUseCase().collect { moviesList ->
                _state.update { currentState ->
                    currentState.copy(
                        movies = moviesList,
                        selectedCount = moviesList.count { it.isSelected }
                    )
                }
            }
        }
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val results = searchMoviesUseCase(query)
                _state.update {
                    it.copy(
                        searchResults = results,
                        isLoading = false,
                        errorMessage = if (results.isEmpty()) "Фильмы не найдены" else null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Ошибка: ${e.message}"
                    )
                }
                _effect.emit(MainEffect.ShowError("Ошибка: ${e.message}"))
            }
        }
    }

    private fun clearSearchResults() {
        _state.update {
            it.copy(
                searchResults = emptyList(),
                errorMessage = null
            )
        }
    }

    private fun addMovie(movie: Movie) {
        viewModelScope.launch {
            insertMovieUseCase(movie)
            _state.update {
                it.copy(
                    currentScreen = Screen.MAIN,
                    selectedMovieForEdit = null
                )
            }
            _effect.emit(MainEffect.NavigateToMain)
        }
    }

    private fun toggleMovieSelection(movie: Movie) {
        viewModelScope.launch {
            toggleMovieSelectionUseCase(movie)
        }
    }

    private fun showDeleteDialog() {
        _state.update { it.copy(isDeleteDialogVisible = true) }
    }

    private fun confirmDelete() {
        viewModelScope.launch {
            try {
                deleteSelectedMoviesUseCase()
                _state.update {
                    it.copy(isDeleteDialogVisible = false)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isDeleteDialogVisible = false) }
                _effect.emit(MainEffect.ShowError("Ошибка при удалении: ${e.message}"))
            }
        }
    }

    private fun dismissDeleteDialog() {
        _state.update { it.copy(isDeleteDialogVisible = false) }
    }

    private fun deleteSelectedMovies() {
        viewModelScope.launch {
            deleteSelectedMoviesUseCase()
        }
    }

    private fun navigateToAdd() {
        viewModelScope.launch {
            _state.update { it.copy(currentScreen = Screen.ADD) }
            _effect.emit(MainEffect.NavigateToAdd(null))
        }
    }

    private fun navigateToEdit(movie: Movie) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    currentScreen = Screen.ADD,
                    selectedMovieForEdit = movie
                )
            }
            _effect.emit(MainEffect.NavigateToAdd(movie))
        }
    }

    private fun navigateToSearch() {
        viewModelScope.launch {
            _state.update { it.copy(currentScreen = Screen.SEARCH) }
            _effect.emit(MainEffect.NavigateToSearch)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            when (_state.value.currentScreen) {
                Screen.ADD -> _state.update {
                    it.copy(
                        currentScreen = Screen.MAIN,
                        selectedMovieForEdit = null
                    )
                }
                Screen.SEARCH -> _state.update {
                    it.copy(currentScreen = Screen.ADD)
                }
                else -> _state.update { it.copy(currentScreen = Screen.MAIN) }
            }
            _effect.emit(MainEffect.NavigateBack)
        }
    }
}