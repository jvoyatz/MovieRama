package com.jvoyatz.movierama.domain.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


data class MovieResults(
    var page: Int = 0,
    var results: List<Movie> = listOf(),
    val totalPages: Int = 1,
    val totalResults: Int = 0,
    val backedResults: MutableList<Movie> = results.toMutableList()
) {
    val query = ""
}

@Parcelize
data class Movie(
    val adult: Boolean = false,
    val backdropPath: String = "",
    val genreIds: List<Int> = listOf(),
    val id: Int = 0,
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String = "",
    val releaseDate: String = "",
    val title: String = "",
    val video: Boolean = false,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
) : Parcelable

@Parcelize
data class MovieDetails(
    val adult: Boolean = false,
    val backdropPath: String = "",
    val belongsToCollection: BelongsToCollection? = null,
    val budget: Int = 0,
    val credits: Credits = Credits(),
    val genres: String = "Not provided",
    val homepage: String = "",
    val id: Int = 0,
    val imdbId: String = "",
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String? = "",
    val productionCompanies: List<ProductionCompany> = listOf(),
    val productionCountries: List<ProductionCountry> = listOf(),
    val releaseDate: String = "",
    val revenue: Int = 0,
    val runtime: Int = 0,
    val spokenLanguages: List<SpokenLanguage> = listOf(),
    val status: String = "",
    val tagline: String = "",
    val title: String = "",
    val video: Boolean = false,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
) : Parcelable

@Parcelize
data class Genre(
    val id: Int = 0,
    val name: String = ""
) : Parcelable

@Parcelize
data class ProductionCompany(
    val id: Int = 0,
    val logoPath: String? = null,
    val name: String = "",
    val originCountry: String = ""
) : Parcelable

@Parcelize
data class ProductionCountry(
    val iso31661: String = "",
    val name: String = ""
) : Parcelable

@Parcelize
data class SpokenLanguage(
    val iso6391: String = "",
    val name: String = ""
) : Parcelable

@Parcelize
data class BelongsToCollection(
    val backdropPath: String = "",
    val id: Int = 0,
    val name: String = "",
    val posterPath: String = ""
) : Parcelable

@Parcelize
data class Cast(
    val adult: Boolean = false,
    val castId: Int = 0,
    val character: String = "",
    val creditId: String = "",
    val gender: Int = 0,
    val id: Int = 0,
    val knownForDepartment: String = "",
    val name: String = "",
    val order: Int = 0,
    val originalName: String = "",
    val popularity: Double = 0.0,
    val profilePath: String? = null
) : Parcelable

@Parcelize
data class Crew(
    val adult: Boolean = false,
    val creditId: String = "",
    val department: String = "",
    val gender: Int = 0,
    val id: Int = 0,
    val job: String = "",
    val knownForDepartment: String = "",
    val name: String = "",
    val originalName: String = "",
    val popularity: Double = 0.0,
    val profilePath: String? = null
) : Parcelable

@Parcelize
data class Credits(
    val cast: List<Cast> = listOf(),
    val crew: List<Crew> = listOf()
) : Parcelable
