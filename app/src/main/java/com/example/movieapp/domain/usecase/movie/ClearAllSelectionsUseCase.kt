// domain/usecase/movie/ClearAllSelectionsUseCase.kt
package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.repository.MovieRepository

class ClearAllSelectionsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() {
        repository.clearAllSelections()
    }
}