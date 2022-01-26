package com.jvoyatz.movierama.domain.models

data class SimilarMovies(
    val page: Int = 0,
    val similarMovies: List<SimilarMovie> = listOf(),
    val totalPages: Int = 0,
    val totalResults: Int = 0
)

data class SimilarMovie(
    val adult: Boolean = false,
    val backdropPath: String? = null,
    val genreIds: List<Int> = listOf(),
    val id: Int = 0,
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String? = null,
    val releaseDate: String = "",
    val title: String = "",
    val video: Boolean = false,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
)