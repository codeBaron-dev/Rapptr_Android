package com.rapptrlabs.androidtest.remote.responsemanager


open class ResponseStateHandler<out R> {
    object Loading : ResponseStateHandler<Nothing>()
    data class Success<out T>(val data: T) : ResponseStateHandler<T>()
    data class Exception(val exception: kotlin.Exception) : ResponseStateHandler<Nothing>()
    data class Error(val message: String?) : ResponseStateHandler<Nothing>()
    data class ThrowableError(val throwable: Throwable): ResponseStateHandler<Nothing>()
}