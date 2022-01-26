package com.jvoyatz.movierama.domain.repository

import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.domain.models.MovieDetails
import com.jvoyatz.movierama.domain.models.MovieReviews
import com.jvoyatz.movierama.domain.models.MovieResults
import com.jvoyatz.movierama.domain.models.SimilarMovies
import kotlinx.coroutines.flow.Flow

interface MoviesRepository{
    fun getPopularMovies(page: Int = 1): Flow<Resource<MovieResults>>
    fun searchMovies(query: String, page: Int = 1):Flow<Resource<MovieResults>>
    fun getMovieById(id:Int):Flow<Resource<MovieDetails>>
    fun getSimilarMoviesById(id:Int, page: Int = 1):Flow<Resource<SimilarMovies>>
    fun getReviewsById(id: Int, page: Int=0):Flow<Resource<MovieReviews>>
}