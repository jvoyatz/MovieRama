package com.jvoyatz.movierama.domain.usecases

import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.domain.models.MovieDetails
import com.jvoyatz.movierama.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetMovieDetails (private val moviesRepository: MoviesRepository) {

    operator fun invoke(movieId: Int): Flow<Resource<MovieDetails>> {
        return moviesRepository.getMovieById(movieId)
    }
}