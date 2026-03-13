package com.example.movieapp.data.local.dao

import androidx.room.*
import com.example.movieapp.data.local.model.MovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object для работы с таблицей movies
 */
@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Insert
    suspend fun insertMovie(movie: MovieEntity)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("DELETE FROM movies WHERE isSelected = 1")
    suspend fun deleteSelectedMovies()

    @Query("UPDATE movies SET isSelected = 0")
    suspend fun clearAllSelections()

    @Query("SELECT COUNT(*) FROM movies WHERE isSelected = 1")
    suspend fun getSelectedCount(): Int
}