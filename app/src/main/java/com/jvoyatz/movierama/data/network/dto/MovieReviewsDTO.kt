package com.jvoyatz.movierama.data.network.dto

import com.jvoyatz.movierama.domain.models.AuthorDetails
import com.jvoyatz.movierama.domain.models.MovieReview
import com.jvoyatz.movierama.domain.models.MovieReviews
import com.squareup.moshi.Json

data class MovieReviewsDTO(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "page")
    val page: Int = 0,
    @Json(name = "results")
    val movieReview: List<MovieReviewDTO> = listOf(),
    @Json(name = "total_pages")
    val totalPages: Int = 0,
    @Json(name = "total_results")
    val totalResults: Int = 0
)

fun MovieReviewsDTO.toDomain():MovieReviews{
    return MovieReviews(
        this.id,
        this.page,
        this.movieReview.map { it.toDomain() },
        this.totalPages,
        this.totalResults
    )
}
data class MovieReviewDTO(
    @Json(name = "author")
    val author: String = "",
    @Json(name = "author_details")
    val authorDetails: AuthorDetailsDTO = AuthorDetailsDTO(),
    @Json(name = "content")
    val content: String = "",
    @Json(name = "created_at")
    val createdAt: String = "",
    @Json(name = "id")
    val id: String = "",
    @Json(name = "updated_at")
    val updatedAt: String = "",
    @Json(name = "url")
    val url: String = ""
)

fun MovieReviewDTO.toDomain():MovieReview{
    return MovieReview(
        this.author,
        this.authorDetails.toDomain(),
        this.content,
        this.createdAt,
        this.id,
        this.updatedAt,
        this.url
    )
}

data class AuthorDetailsDTO(
    @Json(name = "avatar_path")
    val avatarPath: String? = null,
    @Json(name = "name")
    val name: String = "",
    @Json(name = "rating")
    val rating: Any? = null,
    @Json(name = "username")
    val username: String = ""
)

fun AuthorDetailsDTO.toDomain():AuthorDetails{
    return AuthorDetails(
        this.avatarPath,
        this.name,
        this.rating,
        this.username
    )
}