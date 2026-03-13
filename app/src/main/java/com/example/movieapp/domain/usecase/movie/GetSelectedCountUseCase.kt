// domain/usecase/movie/GetSelectedCountUseCase.kt
package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.repository.MovieRepository

class GetSelectedCountUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): Int = repository.getSelectedCount()
}