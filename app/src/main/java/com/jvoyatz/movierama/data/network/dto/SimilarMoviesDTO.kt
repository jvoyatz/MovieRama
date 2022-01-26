package com.jvoyatz.movierama.data.network.dto

import com.jvoyatz.movierama.common.IMG_URL_POSTER
import com.jvoyatz.movierama.domain.models.SimilarMovie
import com.jvoyatz.movierama.domain.models.SimilarMovies
import com.squareup.moshi.Json

data class SimilarMoviesDTO(
    @Json(name = "page")
    val page: Int = 0,
    @Json(name = "results")
    val similarMovies: List<SimilarMovieDTO> = listOf(),
    @Json(name = "total_pages")
    val totalPages: Int = 0,
    @Json(name = "total_results")
    val totalResults: Int = 0
)

fun SimilarMoviesDTO.toDomain():SimilarMovies{
    return SimilarMovies(
            this.page,
            this.similarMovies.map { it.toDomain() },
            this.totalPages,
            this.totalResults
        )
}

data class SimilarMovieDTO(
    @Json(name = "adult")
    val adult: Boolean = false,
    @Json(name = "backdrop_path")
    val backdropPath: String? = null,
    @Json(name = "genre_ids")
    val genreIds: List<Int> = listOf(),
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "original_language")
    val originalLanguage: String = "",
    @Json(name = "original_title")
    val originalTitle: String = "",
    @Json(name = "overview")
    val overview: String = "",
    @Json(name = "popularity")
    val popularity: Double = 0.0,
    @Json(name = "poster_path")
    val posterPath: String? = null,
    @Json(name = "release_date")
    val releaseDate: String = "",
    @Json(name = "title")
    val title: String = "",
    @Json(name = "video")
    val video: Boolean = false,
    @Json(name = "vote_average")
    val voteAverage: Double = 0.0,
    @Json(name = "vote_count")
    val voteCount: Int = 0
)

fun SimilarMovieDTO.toDomain():SimilarMovie {
    return SimilarMovie(
        this.adult,
        "$IMG_URL_POSTER/${this.backdropPath}",
        this.genreIds,
        this.id,
        this.originalLanguage,
        this.originalTitle,
        this.overview,
        this.popularity,
        "${IMG_URL_POSTER}/${this.posterPath}",
        this.releaseDate,
        this.title,
        this.video,
        this.voteAverage,
        this.voteCount
    )
}