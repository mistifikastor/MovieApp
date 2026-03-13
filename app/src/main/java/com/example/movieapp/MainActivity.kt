package com.example.movieapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieapp.data.local.database.MovieDatabase
import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.remote.api.MovieApi
import com.example.movieapp.data.remote.api.RetrofitClient
import com.example.movieapp.data.repository.MovieRepositoryImpl
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.usecase.movie.*
import com.example.movieapp.domain.usecase.search.SearchMoviesUseCase
import com.example.movieapp.presentation.model.MainEffect
import com.example.movieapp.presentation.model.MainIntent
import com.example.movieapp.presentation.model.MainState
import com.example.movieapp.presentation.model.Screen
import com.example.movieapp.presentation.theme.MovieAppTheme
import com.example.movieapp.presentation.view.AddScreen
import com.example.movieapp.presentation.view.MainScreen
import com.example.movieapp.presentation.view.SearchScreen
import com.example.movieapp.presentation.viewmodel.MainViewModel
import com.example.movieapp.presentation.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieAppTheme {
                val repository = provideMovieRepository()
                val viewModel: MainViewModel = viewModel(
                    factory = provideMainViewModelFactory(repository)
                )

                val state by viewModel.state.collectAsState()
                var selectedMovieForEdit by rememberSaveable { mutableStateOf<Movie?>(null) }

                LaunchedEffect(Unit) {
                    viewModel.effect.collect { effect ->
                        handleEffect(effect) { newSelectedMovie ->
                            selectedMovieForEdit = newSelectedMovie
                        }
                    }
                }

                when (state.currentScreen) {
                    Screen.MAIN -> MainScreen(
                        state = state,
                        onIntent = viewModel::handleIntent
                    )
                    Screen.ADD -> AddScreen(
                        state = state.copy(selectedMovieForEdit = selectedMovieForEdit),
                        onIntent = viewModel::handleIntent
                    )
                    Screen.SEARCH -> SearchScreen(
                        state = state,
                        onIntent = viewModel::handleIntent
                    )
                }
            }
        }
    }

    private fun provideMovieRepository(): MovieRepository {
        val database = MovieDatabase.getDatabase(this)
        val movieDao = database.movieDao()
        val movieApi = RetrofitClient.instance
        return MovieRepositoryImpl(movieDao, movieApi, RetrofitClient.API_KEY)
    }

    private fun provideMainViewModelFactory(repository: MovieRepository): MainViewModelFactory {
        return MainViewModelFactory(
            observeMoviesUseCase = ObserveMoviesUseCase(repository),
            insertMovieUseCase = InsertMovieUseCase(repository),
            updateMovieUseCase = UpdateMovieUseCase(repository),
            deleteMovieUseCase = DeleteMovieUseCase(repository),
            deleteSelectedMoviesUseCase = DeleteSelectedMoviesUseCase(repository),
            toggleMovieSelectionUseCase = ToggleMovieSelectionUseCase(repository),
            clearAllSelectionsUseCase = ClearAllSelectionsUseCase(repository),
            getSelectedCountUseCase = GetSelectedCountUseCase(repository),
            searchMoviesUseCase = SearchMoviesUseCase(repository)
        )
    }

    private fun handleEffect(
        effect: MainEffect,
        onSelectedMovieUpdate: (Movie?) -> Unit
    ) {
        when (effect) {
            is MainEffect.NavigateToAdd -> {
                onSelectedMovieUpdate(effect.movie)
            }
            is MainEffect.ShowError -> {
                Toast.makeText(this, effect.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Другие эффекты не требуют обработки
            }
        }
    }
}