// domain/usecase/search/SearchMoviesUseCase.kt
package com.example.movieapp.domain.usecase.search

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository

class SearchMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String): List<Movie> =
        repository.searchMovies(query)
}