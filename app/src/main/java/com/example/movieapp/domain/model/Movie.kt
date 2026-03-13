package com.example.movieapp.domain.model

/**
 * Доменная модель фильма
 * Содержит только бизнес-логику, не зависит от фреймворков
 */
data class Movie(
    val id: Int = 0,
    val title: String,
    val year: String,
    val posterUrl: String,
    val imdbID: String,
    val genre: String? = null,
    val isSelected: Boolean = false
) {
    /**
     * Валидация названия фильма
     */
    fun isValid(): Boolean = title.isNotBlank()

    /**
     * Получение отображаемого жанра
     */
    fun displayGenre(): String = genre ?: "Не указан"
}