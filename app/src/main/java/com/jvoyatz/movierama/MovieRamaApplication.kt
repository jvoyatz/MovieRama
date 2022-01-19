package com.jvoyatz.movierama

import android.app.Application
import timber.log.Timber

class MovieRamaApplication:Application() {
    override fun onCreate() {
        super.onCreate()
//        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree())
//        }
    }
}