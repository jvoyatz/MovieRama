package com.jvoyatz.movierama.ui.movies


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.domain.models.MovieDetails
import com.jvoyatz.movierama.domain.models.MovieResults
import com.jvoyatz.movierama.domain.usecases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "MoviesViewModel"
@ExperimentalCoroutinesApi
@HiltViewModel
class MoviesViewModel @Inject constructor(private val useCases: UseCases): ViewModel() {

    private val movieSearchStateFlow = MutableStateFlow("")

    private var _moviesState: MutableStateFlow<Resource<MovieResults>> = MutableStateFlow(Resource.Init)
    val moviesState: StateFlow<Resource<MovieResults>> = _moviesState

    private var _moviesDetailsResponse: MutableStateFlow<Resource<MovieDetails>> = MutableStateFlow(Resource.Init)
    val movieDetailsState: StateFlow<Resource<MovieDetails>> = _moviesDetailsResponse


    init {
        viewModelScope.launch {
            movieSearchStateFlow
                .flatMapLatest { query ->
                when(query.isEmpty()){
                    true -> {
                        useCases.getPopularMovies()
                    }
                    else -> {
                       useCases.searchForMovies(query)
                    }
                }
            }
            .onStart {
                emit(Resource.Loading)
                delay(1000)
            }
            .collect {
                _moviesState.value = it
            }
        }
    }


    fun getPopularMovies(){
        viewModelScope.launch {
            useCases.getPopularMovies()
                .onStart {
                    emit(Resource.Loading)
                }
                .collect {
                    it.let {
                        _moviesState.value = it
                    }
                }
        }
    }

    fun getMovieDetails(id: Int){
        viewModelScope.launch {
            useCases.getMovieDetails(id)
                //.onStart {//SHOW LOADING}
                .collectLatest{
                    _moviesDetailsResponse.value = it
                }
        }
    }

    fun searchForMovies(query: String?) {
        query?.let {
            movieSearchStateFlow.value = it
        }
    }
}