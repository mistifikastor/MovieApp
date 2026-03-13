package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.repository.MovieRepository

/**
 * UseCase для удаления всех выбранных фильмов
 */
class DeleteSelectedMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() {
        repository.deleteSelectedMovies()
    }
}