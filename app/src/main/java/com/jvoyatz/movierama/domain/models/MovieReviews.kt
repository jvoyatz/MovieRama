package com.jvoyatz.movierama.domain.models

data class MovieReviews(
    val id: Int = 0,
    val page: Int = 0,
    val movieReview: List<MovieReview> = listOf(),
    val totalPages: Int = 0,
    val totalResults: Int = 0
)

data class MovieReview(
    val author: String = "",
    val authorDetails: AuthorDetails = AuthorDetails(),
    val content: String = "",
    val createdAt: String = "",
    val id: String = "",
    val updatedAt: String = "",
    val url: String = ""
)

data class AuthorDetails(
    val avatarPath: String? = null,
    val name: String = "",
    val rating: Any? = null,
    val username: String = ""
)