package com.jvoyatz.movierama.ui.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.domain.models.Movie
import com.jvoyatz.movierama.domain.models.MovieDetails
import com.jvoyatz.movierama.domain.models.MovieReviews
import com.jvoyatz.movierama.domain.models.SimilarMovies
import com.jvoyatz.movierama.domain.usecases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "DetailsViewModel"

@HiltViewModel
class DetailsViewModel @Inject constructor(val useCases: UseCases,
    private val stateHandle: SavedStateHandle): ViewModel() {
    private val _reviewsState: MutableStateFlow<Resource<MovieReviews>> = MutableStateFlow(Resource.Init)
    val reviewsState: StateFlow<Resource<MovieReviews>> = _reviewsState

    private val _similarMoviesState: MutableStateFlow<Resource<SimilarMovies>> = MutableStateFlow(Resource.Init)
    val similarMoviesState: StateFlow<Resource<SimilarMovies>> = _similarMoviesState

    init {
        stateHandle.get<MovieDetails>("movie")?.let{
            Log.d(TAG, "${it.id}: ")
            getMoviesReviews(it.id)
            getSimilarMovies(it.id)
        }
    }

    fun getMoviesReviews(id: Int){
        viewModelScope.launch {
            useCases.getMovieReviews(id)
                .collect {
                    _reviewsState.value = it
                }
        }
    }

    fun getSimilarMovies(id: Int){
        viewModelScope.launch {
            useCases.getSimilarMovies(id)
                .collect {
                    _similarMoviesState.value = it
                }
        }
    }
}