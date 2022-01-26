package com.jvoyatz.movierama.data.network

import com.jvoyatz.movierama.common.QUERY_PARAM_TOKEN
import com.jvoyatz.movierama.common.TOKEN
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named


class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        var httpUrl = request.url.newBuilder()
                .addQueryParameter(QUERY_PARAM_TOKEN, TOKEN)
                .build()

        val updatedRequest =
            request.newBuilder()
                .url(httpUrl)
                .build()

        return chain.proceed(updatedRequest)
    }
}