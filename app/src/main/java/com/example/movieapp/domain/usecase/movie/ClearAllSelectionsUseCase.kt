package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.repository.MovieRepository

/**
 * UseCase для снятия всех выделений
 */
class ClearAllSelectionsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() {
        repository.clearAllSelections()
    }
}