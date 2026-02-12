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
import com.example.movieapp.model.MovieRepository
import com.example.movieapp.view.AddScreen
import com.example.movieapp.view.MainScreen

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

                when (currentScreen) {
                    Screen.MAIN -> MainScreen(
                        controller = controller,
                        onAddClick = { currentScreen = Screen.ADD }
                    )
                    Screen.ADD -> AddScreen(
                        controller = controller,
                        onBack = { currentScreen = Screen.MAIN }
                    )
                }
            }
        }
    }
}

enum class Screen {
    MAIN, ADD
}