package com.jvoyatz.movierama.data.network

import com.jvoyatz.movierama.common.TOKEN
import com.jvoyatz.movierama.data.network.dto.MovieDetailsDTO
import com.jvoyatz.movierama.data.network.dto.MovieReviewsDTO
import com.jvoyatz.movierama.data.network.dto.MoviesDTO
import com.jvoyatz.movierama.data.network.dto.SimilarMoviesDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") token:String=TOKEN , @Query("page") page: Int = 1): Response<MoviesDTO>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String?,
        @Query("page") page: Int = 1,
        @Query("api_key") token:String=TOKEN
    ): Response<MoviesDTO>

    @GET("movie/{movie_id}")
    suspend fun getMovieById( @Path("movie_id") id: Int = 1,  @Query("append_to_response") value:String="credits", @Query("api_key") token:String=TOKEN):Response<MovieDetailsDTO>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMoviesById(@Path("movie_id") id: Int, @Query("api_key") token:String=TOKEN):Response<SimilarMoviesDTO>

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviewsById(@Path("movie_id") id: Int,
                               @Query("page") page: Int = 1, @Query("api_key") token:String=TOKEN):Response<MovieReviewsDTO>
}