package com.jvoyatz.movierama.data.network.dto

import android.util.Log
import com.jvoyatz.movierama.common.IMG_URL_BG
import com.jvoyatz.movierama.common.IMG_URL_POSTER
import com.jvoyatz.movierama.domain.models.*
import com.squareup.moshi.Json

data class MoviesDTO(
    @Json(name = "page")
    val page: Int = 0,
    @Json(name = "results")
    val results: List<MovieDTO> = listOf(),
    @Json(name = "total_pages")
    val totalPages: Int = 0,
    @Json(name = "total_results")
    val totalResults: Int = 0
)

private const val TAG = "MoviesDTO"
fun MoviesDTO.toDomain():MovieResults{
    return MovieResults(
        this.page,
        this.results.map {
            it.toDomain()
        },
        this.totalPages,
        this.totalResults
    )
}

data class MovieDTO(
    @Json(name = "adult")
    val adult: Boolean = false,
    @Json(name = "backdrop_path")
    val backdropPath: String? = "",
    @Json(name = "genre_ids")
    val genreIds: List<Int> = listOf(),
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "original_language")
    val originalLanguage: String = "",
    @Json(name = "original_title")
    val originalTitle: String = "",
    @Json(name = "overview")
    val overview: String = "",
    @Json(name = "popularity")
    val popularity: Double = 0.0,
    @Json(name = "poster_path")
    val posterPath: String? = "",
    @Json(name = "release_date")
    val releaseDate: String = "",
    @Json(name = "title")
    val title: String = "",
    @Json(name = "video")
    val video: Boolean = false,
    @Json(name = "vote_average")
    val voteAverage: Double = 0.0,
    @Json(name = "vote_count")
    val voteCount: Int = 0
)

fun MovieDTO.toDomain():Movie{
    return Movie(
        this.adult,
        "${IMG_URL_BG}/${this.backdropPath}",
        this.genreIds,
        this.id,
        this.originalLanguage,
        this.originalTitle,
        this.overview,
        this.popularity,
        "${IMG_URL_POSTER}/${this.posterPath}",
        this.releaseDate,
        this.title,
        this.video,
        this.voteAverage,
        this.voteCount
    )
}

data class MovieDetailsDTO(
    @Json(name = "adult")
    val adult: Boolean = false,
    @Json(name = "backdrop_path")
    val backdropPath: String = "",
    @Json(name = "belongs_to_collection")
    val belongsToCollection: BelongsToCollection? = null,
    @Json(name = "budget")
    val budget: Int = 0,
    @Json(name = "credits")
    val credits: CreditsDTO = CreditsDTO(),
    @Json(name = "genres")
    val genres: List<Genre> = listOf(),
    @Json(name = "homepage")
    val homepage: String = "",
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "imdb_id")
    val imdbId: String = "",
    @Json(name = "original_language")
    val originalLanguage: String = "",
    @Json(name = "original_title")
    val originalTitle: String = "",
    @Json(name = "overview")
    val overview: String = "",
    @Json(name = "popularity")
    val popularity: Double = 0.0,
    @Json(name = "poster_path")
    val posterPath: String = "",
    @Json(name = "production_companies")
    val productionCompanies: List<ProductionCompanyDTO> = listOf(),
    @Json(name = "production_countries")
    val productionCountries: List<ProductionCountryDTO> = listOf(),
    @Json(name = "release_date")
    val releaseDate: String = "",
    @Json(name = "revenue")
    val revenue: Int = 0,
    @Json(name = "runtime")
    val runtime: Int = 0,
    @Json(name = "spoken_languages")
    val spokenLanguages: List<SpokenLanguageDTO> = listOf(),
    @Json(name = "status")
    val status: String = "",
    @Json(name = "tagline")
    val tagline: String = "",
    @Json(name = "title")
    val title: String = "",
    @Json(name = "video")
    val video: Boolean = false,
    @Json(name = "vote_average")
    val voteAverage: Double = 0.0,
    @Json(name = "vote_count")
    val voteCount: Int = 0
)

fun MovieDetailsDTO.toDomain(): MovieDetails {
    return MovieDetails(
        this.adult,
        "${IMG_URL_BG}/${this.backdropPath}",
        this.belongsToCollection,
        this.budget,
        this.credits.toDomain(),
        this.genres.joinToString { it.name },
        this.homepage,
        this.id,
        this.imdbId,
        this.originalLanguage,
        this.originalTitle,
        this.overview,
        this.popularity,
        "${IMG_URL_POSTER}/${this.posterPath}",
        this.productionCompanies.map { it.toDomain() },
        this.productionCountries.map { it.toDomain() },
        this.releaseDate,
        this.revenue,
        this.runtime,
        this.spokenLanguages.map { it.toDomain()},
        this.status,
        this.tagline,
        this.title,
        this.video,
        this.voteAverage,
        this.voteCount
    )
}

data class BelongsToCollectionDTO(
    @Json(name = "backdrop_path")
    val backdropPath: String = "",
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "name")
    val name: String = "",
    @Json(name = "poster_path")
    val posterPath: String = ""
)

fun BelongsToCollectionDTO.toDomain():BelongsToCollection{
    return BelongsToCollection(
        this.backdropPath,
        this.id,
        this.name,
        this.posterPath
    )
}

data class CreditsDTO(
    @Json(name = "cast")
    val cast: List<CastDTO> = listOf(),
    @Json(name = "crew")
    val crew: List<CrewDTO> = listOf()
)

fun CreditsDTO.toDomain(): Credits{
    var cast = this.cast.map { it.toDomain() }
    val crew = this.crew.map { it.toDomain() }
    return Credits(
        cast,
        crew
    )
}

data class GenreDTO(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "name")
    val name: String = ""
)

fun GenreDTO.toDomain(): Genre{
    return Genre(this.id, this.name)
}

data class ProductionCompanyDTO(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "logo_path")
    val logoPath: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "origin_country")
    val originCountry: String = ""
)

fun ProductionCompanyDTO.toDomain(): ProductionCompany{
    return ProductionCompany(this.id, this.logoPath, this.name, this.originCountry)
}

data class ProductionCountryDTO(
    @Json(name = "iso_3166_1")
    val iso31661: String = "",
    @Json(name = "name")
    val name: String = ""
)

fun ProductionCountryDTO.toDomain(): ProductionCountry{
    return ProductionCountry(this.iso31661, this.name)
}

data class SpokenLanguageDTO(
    @Json(name = "english_name")
    val englishName: String = "",
    @Json(name = "iso_639_1")
    val iso6391: String = "",
    @Json(name = "name")
    val name: String = ""
)

fun SpokenLanguageDTO.toDomain(): SpokenLanguage {
    return SpokenLanguage(iso6391, name)
}

data class CastDTO(
    @Json(name = "adult")
    val adult: Boolean = false,
    @Json(name = "cast_id")
    val castId: Int = 0,
    @Json(name = "character")
    val character: String = "",
    @Json(name = "credit_id")
    val creditId: String = "",
    @Json(name = "gender")
    val gender: Int = 0,
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "known_for_department")
    val knownForDepartment: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "order")
    val order: Int = 0,
    @Json(name = "original_name")
    val originalName: String = "",
    @Json(name = "popularity")
    val popularity: Double = 0.0,
    @Json(name = "profile_path")
    val profilePath: String? = null
)

fun CastDTO.toDomain(): Cast{
    return Cast(
        adult,
        castId,
        character,
        creditId,
        gender,
        id,
        knownForDepartment,
        name,
        order,
        originalName,
        popularity,
        profilePath
    )
}

data class CrewDTO(
    @Json(name = "adult")
    val adult: Boolean = false,
    @Json(name = "credit_id")
    val creditId: String = "",
    @Json(name = "department")
    val department: String = "",
    @Json(name = "gender")
    val gender: Int = 0,
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "job")
    val job: String = "",
    @Json(name = "known_for_department")
    val knownForDepartment: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "original_name")
    val originalName: String = "",
    @Json(name = "popularity")
    val popularity: Double = 0.0,
    @Json(name = "profile_path")
    val profilePath: String? = null
)

fun CrewDTO.toDomain():Crew{
    return Crew(
        this.adult,
        this.creditId,
        this.department,
        this.gender,
        this.id,
        this.job,
        this.knownForDepartment,
        this.name,
        this.originalName,
        this.popularity,
        this.profilePath
    )
}
