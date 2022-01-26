package com.jvoyatz.movierama.ui.movies


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.domain.models.MovieDetails
import com.jvoyatz.movierama.domain.models.MovieResults
import com.jvoyatz.movierama.domain.usecases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MoviesViewModel"
@HiltViewModel
class MoviesViewModel @Inject constructor(private val useCases: UseCases): ViewModel() {

    private var _popularMoviesResponse: MutableStateFlow<Resource<MovieResults>> = MutableStateFlow(Resource.Init)
    val popularMoviesState: StateFlow<Resource<MovieResults>> = _popularMoviesResponse

    private var _moviesDetailsResponse: MutableStateFlow<Resource<MovieDetails>> = MutableStateFlow(Resource.Init)
    val movieDetailsState: StateFlow<Resource<MovieDetails>> = _moviesDetailsResponse

    init {
        viewModelScope.launch {
            useCases.getPopularMovies()
                .onStart {
                    emit(Resource.Loading)
                }
                .collect {
                    it.let {
                        _popularMoviesResponse.value = it
                    }
            }
        }
    }

    fun getMovieDetails(id: Int){
        viewModelScope.launch {
            useCases.getMovieDetails(id)
                .onStart {  }
                .collect {
                    Log.d(TAG, "getMovieDetails: $it")
                    _moviesDetailsResponse.value = it
                }
        }
    }
}