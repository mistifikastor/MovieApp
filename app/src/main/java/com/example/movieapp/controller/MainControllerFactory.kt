// app/src/main/java/com/example/movieapp/controller/MainControllerFactory.kt
package com.example.movieapp.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.model.MovieRepository

class MainControllerFactory(
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainController::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainController(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}