package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository

/**
 * UseCase для добавления нового фильма
 */
class InsertMovieUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        // Можно добавить бизнес-логику перед сохранением
        require(movie.isValid()) { "Movie must have a title" }
        repository.insertMovie(movie)
    }
}