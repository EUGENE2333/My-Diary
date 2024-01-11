package com.example.mydiary.data.repository

import android.util.Log
import com.example.mydiary.data.repository.Resources.*

sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null
){
    class Loading<T>: Resources<T>()
    class Success<T>(data: T?): Resources<T>(data = data)
    class Error<T>(throwable: Throwable?): Resources<T>(throwable = throwable)
}


/**
 * Wraps api calls into a [Resources]. If the call throws an exception, then the exception is wrapped into an [AppError].
 *
 * @param apiCall the api call passed as a lambda function
 * @param T is the api call response type
 */
suspend fun <T> executeRequest(apiCall: suspend () -> T): Resources<T> {
    return try {
        Success(apiCall())
    } catch (e: Throwable) {
        Log.d("executeRequest", e.toString())
       Error(e)

    }
}
