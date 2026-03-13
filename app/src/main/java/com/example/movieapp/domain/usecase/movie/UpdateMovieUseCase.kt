// domain/usecase/movie/UpdateMovieUseCase.kt
package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository

class UpdateMovieUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.updateMovie(movie)
    }
}