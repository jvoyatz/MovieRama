package com.jvoyatz.movierama.domain.usecases

import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.domain.models.MovieReviews
import com.jvoyatz.movierama.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetMovieReviews(private val moviesRepository: MoviesRepository) {

    operator fun invoke(movieId: Int): Flow<Resource<MovieReviews>> {
        return moviesRepository.getReviewsById(movieId)
    }
}