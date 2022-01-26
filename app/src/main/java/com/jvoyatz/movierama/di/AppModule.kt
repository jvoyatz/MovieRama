package com.jvoyatz.movierama.di

import android.content.Context
import androidx.room.Room
import com.jvoyatz.movierama.common.URL
import com.jvoyatz.movierama.data.MoviesRepositoryImpl
import com.jvoyatz.movierama.data.database.MovieDatabase
import com.jvoyatz.movierama.data.database.MoviesDao
import com.jvoyatz.movierama.data.network.AuthInterceptor
import com.jvoyatz.movierama.data.network.MoviesApiService
import com.jvoyatz.movierama.domain.repository.MoviesRepository
import com.jvoyatz.movierama.domain.usecases.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * There are several ways to provide injection using hilt
 * 1) constructor injection. hilt knows this way how to provide instances of this class
 *      hilt should be aware of how to provide argument-types found in the constructor
 * !!!NOTE!!! Module classes like this found here, let Hilt know how to provide instances for particular types.
 * For instance, you might dont own some types, so you are not able to injection using the first way as described before,
 * thus you can inform HIlt how to provide these types using modules
 *
 * 2) If you have an interface, for instance a Repository interface, you are not able to inject it using the constructor.
 * In this case you can use @Binds. More to be added here!!!!!!!!!!
 * Using binds you can inform Hilt so as to be able to know implementation to use to provide
 * an instance of the given interface
 *
 * 3) Another option is the @Provides annotation
 */

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideItemsDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movies_database")
            .build()
    }

    @Provides
    @Singleton
    fun provideItemsDao(db: MovieDatabase): MoviesDao {
        return db.itemsDao
    }

    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttp(interceptor: HttpLoggingInterceptor, authInterceptor: AuthInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                //.dns(Dns.SYSTEM)
                .addInterceptor(interceptor)
                //.addInterceptor(authInterceptor)
                .retryOnConnectionFailure(true)
                .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    fun provideMovieService(retrofit: Retrofit): MoviesApiService
        = retrofit.create(MoviesApiService::class.java)

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideMoviesRepository(moviesApi: MoviesApiService, ioDispatcher: CoroutineDispatcher, moviesDao: MoviesDao):
            MoviesRepository = MoviesRepositoryImpl(moviesApi, moviesDao, ioDispatcher )

    @Singleton
    @Provides
    fun providesInteractor(moviesRepository: MoviesRepository): UseCases{
        return UseCases(
            GetPopularMovies(moviesRepository),
            SearchForMovies(moviesRepository),
            GetMovieDetails(moviesRepository),
            GetSimilarMovies(moviesRepository),
            GetMovieReviews(moviesRepository),
            ResetSearchQuery(moviesRepository),
            MarkFavoriteMovie(repository = moviesRepository),
        )
    }
}