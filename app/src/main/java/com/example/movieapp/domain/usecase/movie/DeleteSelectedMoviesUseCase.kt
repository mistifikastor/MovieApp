// domain/usecase/movie/DeleteSelectedMoviesUseCase.kt
package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.repository.MovieRepository

class DeleteSelectedMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() {
        repository.deleteSelectedMovies()
    }
}