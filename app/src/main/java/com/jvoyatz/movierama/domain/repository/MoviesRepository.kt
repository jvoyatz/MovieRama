package com.jvoyatz.movierama.domain.repository

import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.domain.models.MovieDetails
import com.jvoyatz.movierama.domain.models.MovieReviews
import com.jvoyatz.movierama.domain.models.MovieResults
import com.jvoyatz.movierama.domain.models.SimilarMovies
import kotlinx.coroutines.flow.Flow

interface MoviesRepository{
    fun getPopularMovies(): Flow<Resource<MovieResults>>
    fun searchMovies(query: String):Flow<Resource<MovieResults>>
    fun getMovieById(id:Int):Flow<Resource<MovieDetails>>
    fun getSimilarMoviesById(id:Int):Flow<Resource<SimilarMovies>>
    fun getReviewsById(id: Int):Flow<Resource<MovieReviews>>
    fun resetSearchQuery()
    suspend fun markFavoriteMovie(id: Int, name: String): Flow<Boolean>
}