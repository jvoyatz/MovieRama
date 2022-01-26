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
import com.jvoyatz.movierama.domain.usecases.SearchForMovies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import retrofit2.Response
import java.net.UnknownHostException

private const val TAG = "MoviesRepositoryImpl"

class MoviesRepositoryImpl(private val moviesApi: MoviesApiService, private val ioDispatcher: CoroutineDispatcher): MoviesRepository {

    private var popularMovieResults: MovieResults = MovieResults()
    private var searchMovieResults: MovieResults = MovieResults()


    override fun getPopularMovies(): Flow<Resource<MovieResults>> {

        var nextPage = if( popularMovieResults.page == 0) { 1  }else {
                popularMovieResults.page + 1
        }

        if (nextPage > (popularMovieResults.totalPages)){
            return flow {
                emit(Resource.Success(popularMovieResults))
            }
        }

        return apiCallFlow({
            moviesApi.getPopularMovies(page = nextPage)
        }){
            popularMovieResults = it.toDomain(popularMovieResults.results)
            popularMovieResults
        }
    }

    override fun searchMovies(query: String):Flow<Resource<MovieResults>> {
        var nextPage = if( searchMovieResults.page == 0) { 1  }else {
            searchMovieResults.page + 1
        }

        if (nextPage > (searchMovieResults.totalPages)){
            return flow {
                emit(Resource.Success(searchMovieResults))
            }
        }

        return apiCallFlow({
            moviesApi.searchMovies(query, nextPage)
        }){
            searchMovieResults = it.toDomain()
            searchMovieResults
        }
    }

    override fun getMovieById(id: Int): Flow<Resource<MovieDetails>> {
        return apiCallFlow({
            moviesApi.getMovieById(id = id)
        }){
            it.toDomain()
        }
    }

    override fun getSimilarMoviesById(id: Int):Flow<Resource<SimilarMovies>> {
        return apiCallFlow({ moviesApi.getSimilarMoviesById(id) }){
            it.toDomain()
        }
    }

    override fun getReviewsById(id: Int): Flow<Resource<MovieReviews>> {
        return apiCallFlow({
            moviesApi.getReviewsById(id)
        }) {
            it.toDomain()
        }
    }

    override fun resetSearchQuery() {
        searchMovieResults = MovieResults()
    }

    private fun <T, R> apiCallFlow(call: suspend () -> Response<T>?, toDomain: suspend (T) -> R): Flow<Resource<R>> {
        return flow {
            try {
                call()?.let {
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
                }
            } catch (e: Exception) {
               // e.printStackTrace()
                emit(Resource.Error.create(e))
            }
        }
        .flowOn(ioDispatcher)
    }
}