package com.jvoyatz.movierama.domain.usecases

class UseCases(
    val getPopularMovies: GetPopularMovies,
    val searchForMovies: SearchForMovies,
    val getMovieDetails: GetMovieDetails,
    val getSimilarMovies: GetSimilarMovies,
    val getMovieReviews: GetMovieReviews,
    val resetSearchQuery: ResetSearchQuery,
)