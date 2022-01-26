package com.jvoyatz.movierama.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey
    val movieId: Int,
    val name: String
)