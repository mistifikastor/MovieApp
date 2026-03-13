package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.repository.MovieRepository

/**
 * UseCase для получения количества выбранных фильмов
 */
class GetSelectedCountUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): Int = repository.getSelectedCount()
}