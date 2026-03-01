// app/src/main/java/com/example/movieapp/MainActivity.kt
package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.movieapp.model.MovieRepository
import com.example.movieapp.ui.main.MainEffect
import com.example.movieapp.ui.main.MainIntent
import com.example.movieapp.ui.main.MainViewModel
import com.example.movieapp.view.AddScreen
import com.example.movieapp.view.MainScreen
import com.example.movieapp.view.SearchScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                // Создаём ViewModel
                val repository = MovieRepository(applicationContext)
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(repository)
                )

                // Собираем состояние
                val state by viewModel.state.collectAsState()

                // Обрабатываем эффекты (одноразовые события)
                LaunchedEffect(Unit) {
                    viewModel.effect.collect { effect ->
                        handleEffect(effect)
                    }
                }

                // Отображаем текущий экран
                when (state.currentScreen) {
                    Screen.MAIN -> MainScreen(
                        state = state,
                        onIntent = viewModel::handleIntent
                    )

                    Screen.ADD -> AddScreen(
                        state = state,
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

    private fun handleEffect(effect: MainEffect) {
        when (effect) {
            is MainEffect.NavigateToMain -> {
                // Обновляем состояние через Intent
                // Но так как у нас нет прямого доступа к ViewModel здесь,
                // лучше использовать Effect для навигации и обновлять состояние через Intent
            }
            is MainEffect.NavigateToAdd -> {
                // Навигация + обновление selectedMovieForEdit
            }
            is MainEffect.NavigateToSearch -> {
                // Навигация
            }
            is MainEffect.NavigateBack -> {
                // Назад
            }
            is MainEffect.ShowError -> {
                // Показать тост или снекбар
            }
        }
    }
}

// Factory для ViewModel
class MainViewModelFactory(
    private val repository: MovieRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}