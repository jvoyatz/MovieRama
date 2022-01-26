package com.jvoyatz.movierama.domain.usecases

import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.domain.models.MovieResults
import com.jvoyatz.movierama.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow


class GetPopularMovies(private val moviesRepository: MoviesRepository) {
    operator fun invoke(page: Int = 1): Flow<Resource<MovieResults>> {
        return moviesRepository.getPopularMovies(page)
    }
}