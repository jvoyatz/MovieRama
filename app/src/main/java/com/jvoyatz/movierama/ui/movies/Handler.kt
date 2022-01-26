package com.jvoyatz.movierama.ui.movies

import com.jvoyatz.movierama.domain.models.Movie

interface Handler {
    fun markMovieAsFavorite(position: Int)
}