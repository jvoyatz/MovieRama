package com.jvoyatz.movierama.domain.usecases

class UseCases(
    val getPopularMovies: GetPopularMovies,
    val getMovieDetails: GetMovieDetails,
    val getSimilarMovies: GetSimilarMovies,
    val getMovieReviews: GetMovieReviews,
)