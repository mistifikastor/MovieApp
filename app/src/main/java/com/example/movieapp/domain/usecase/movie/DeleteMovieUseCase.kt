package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository

/**
 * UseCase для удаления одного фильма
 */
class DeleteMovieUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        require(movie.id != 0) { "Movie must have an ID for deletion" }
        repository.deleteMovie(movie)
    }
}