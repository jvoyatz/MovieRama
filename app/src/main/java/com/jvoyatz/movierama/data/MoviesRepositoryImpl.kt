package com.jvoyatz.movierama.data

import android.util.Log
import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.data.network.MoviesApiService
import com.jvoyatz.movierama.data.network.dto.toDomain
import com.jvoyatz.movierama.domain.models.MovieDetails
import com.jvoyatz.movierama.domain.models.MovieResults
import com.jvoyatz.movierama.domain.models.MovieReviews
import com.jvoyatz.movierama.domain.models.SimilarMovies
import com.jvoyatz.movierama.domain.repository.MoviesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

private const val TAG = "MoviesRepositoryImpl"

class MoviesRepositoryImpl(private val moviesApi: MoviesApiService, private val ioDispatcher: CoroutineDispatcher): MoviesRepository {

    override fun getPopularMovies(page: Int): Flow<Resource<MovieResults>> {
        return apiCallFlow({
            moviesApi.getPopularMovies()
        }){
            it.toDomain()
        }
    }

    override fun searchMovies(query: String, page: Int):Flow<Resource<MovieResults>> {
        return apiCallFlow({
            moviesApi.searchMovies(query)
        }){
            it.toDomain()
        }
    }

    override fun getMovieById(id: Int): Flow<Resource<MovieDetails>> {
        return apiCallFlow({
            moviesApi.getMovieById(id = id)
        }){
            it.toDomain()
        }
    }

    override fun getSimilarMoviesById(id: Int, page: Int):Flow<Resource<SimilarMovies>> {
        return apiCallFlow({ moviesApi.getSimilarMoviesById(id) }){
            it.toDomain()
        }
    }

    override fun getReviewsById(id: Int, page: Int): Flow<Resource<MovieReviews>> {
        return apiCallFlow({
            moviesApi.getReviewsById(id)
        }) {
            it.toDomain()
        }
    }

    private fun <T, R> apiCallFlow(call: suspend () -> Response<T>?, toDomain: suspend (T) -> R): Flow<Resource<R>> {
        return flow {
            call()?.let {
                try {
                    when (it.isSuccessful) {
                        true -> {
                            it.body()?.let { body ->
                                body.toString()
                                emit(Resource.Success(toDomain(body)))
                            }
                        }
                        else -> {
                            it.errorBody()?.let { error ->
                                val errorMsg = error.string()
                                error.close()
                                emit(Resource.Error(message = errorMsg))
                            }
                        }
                    }
                } catch (e: Exception) {
                    emit(Resource.Error.create(e))
                }
            }
        }
            .flowOn(ioDispatcher)
    }
}