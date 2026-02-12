// app/src/main/java/com/example/movieapp/MainActivity.kt
package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieapp.controller.MainController
import com.example.movieapp.controller.MainControllerFactory
import com.example.movieapp.model.Movie
import com.example.movieapp.model.MovieRepository
import com.example.movieapp.view.AddScreen
import com.example.movieapp.view.MainScreen
import com.example.movieapp.view.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val repository = MovieRepository(applicationContext)
                val controller: MainController = viewModel(
                    factory = MainControllerFactory(repository)
                )

                var currentScreen by remember { mutableStateOf(Screen.MAIN) }
                var selectedMovieForEdit by remember { mutableStateOf<Movie?>(null) }

                when (currentScreen) {
                    Screen.MAIN -> MainScreen(
                        controller = controller,
                        onAddClick = {
                            selectedMovieForEdit = null
                            currentScreen = Screen.ADD
                        }
                    )

                    Screen.ADD -> AddScreen(
                        controller = controller,
                        onBack = { currentScreen = Screen.MAIN },
                        onOpenSearch = { currentScreen = Screen.SEARCH },
                        selectedMovie = selectedMovieForEdit
                    )

                    Screen.SEARCH -> SearchScreen(
                        controller = controller,
                        onBack = { currentScreen = Screen.ADD },
                        onMovieSelected = { movie ->
                            selectedMovieForEdit = movie
                            currentScreen = Screen.ADD
                        }
                    )
                }
            }
        }
    }
}

enum class Screen {
    MAIN, ADD, SEARCH
}