package com.jvoyatz.movierama.ui.details

import com.jvoyatz.movierama.domain.models.Movie
import com.jvoyatz.movierama.domain.models.MovieDetails

interface DetailsHandler {
    fun markMovieAsFavorite(movie: MovieDetails)
}