package com.jvoyatz.movierama.common

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * used to wrap elements displayed in the ui
 */
sealed class Resource<out T>() {
    object Init: Resource<Nothing>()
    object Loading: Resource<Nothing>()
    data class Success<T>(val data:T): Resource<T>()
    data class Error(val message: String): Resource<Nothing>(){

        companion object{
            fun create(t: Throwable): Resource.Error {
                var message: String = "Uknown Error Exception"

                when (t) {
                    is SocketTimeoutException -> message = "Connection Error"
                    is ConnectException -> message = "No internet access"
                    is UnknownHostException -> message = "No internet access!"
                    is HttpException -> {
                        when(t.code()){
                            in 500 .. 502 -> message = "Internal server error"
                            400 -> message = "Bad Request"
                            404 -> message = "Not Found"
                            403 -> message = "Forbidden"
                            401 -> message = "Not AUthenticated"
                        }
                    }
                    else -> {
                        message = "Uknown Error Exception"
                    }
                }
                return Resource.Error(message)
            }
        }
    }
}