// domain/usecase/movie/DeleteMovieUseCase.kt
package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository

class DeleteMovieUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.deleteMovie(movie)
    }
}