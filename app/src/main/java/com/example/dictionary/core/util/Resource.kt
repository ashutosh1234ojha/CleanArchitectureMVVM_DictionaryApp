package com.example.dictionary.core.util

sealed class Resource<T>( val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data, null)
    class Success<T>(data: T?) : Resource<T>(data, null)
    class Failure<T>(data: T? = null, message: String?) : Resource<T>(data, message)
}