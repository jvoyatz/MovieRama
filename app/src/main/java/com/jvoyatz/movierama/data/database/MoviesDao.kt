package com.jvoyatz.movierama.data.database

import androidx.room.*
import com.jvoyatz.movierama.domain.models.Movie

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: FavoriteMovieEntity)

    @Query("SELECT * FROM favorite_movies WHERE movieId = :movieId")
    suspend fun getFavoriteMovie(movieId: Int): FavoriteMovieEntity?
}