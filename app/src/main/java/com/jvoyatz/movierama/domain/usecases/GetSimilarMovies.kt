package com.jvoyatz.movierama.domain.usecases

import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.domain.models.MovieReviews
import com.jvoyatz.movierama.domain.models.SimilarMovies
import com.jvoyatz.movierama.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetSimilarMovies(val moviesRepository: MoviesRepository) {
    operator fun invoke(movieId: Int, page: Int = 0): Flow<Resource<SimilarMovies>> {
        return moviesRepository.getSimilarMoviesById(movieId, page)
    }
}