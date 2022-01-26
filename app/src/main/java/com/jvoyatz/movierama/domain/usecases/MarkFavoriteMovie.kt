package com.jvoyatz.movierama.domain.usecases

import com.jvoyatz.movierama.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class MarkFavoriteMovie(private val repository: MoviesRepository) {
    suspend operator fun invoke(id: Int, name: String): Flow<Boolean> {
        return repository.markFavoriteMovie(id, name)
    }
}