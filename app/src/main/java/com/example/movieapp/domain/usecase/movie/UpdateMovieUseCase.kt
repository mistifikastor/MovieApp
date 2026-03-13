package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository

/**
 * UseCase для обновления фильма
 */
class UpdateMovieUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        require(movie.id != 0) { "Movie must have an ID for update" }
        require(movie.isValid()) { "Movie must have a title" }
        repository.updateMovie(movie)
    }
}