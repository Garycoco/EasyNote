package com.example.easynote.common

sealed class Response<out T> {
    object Loading: Response<Nothing>()
    data class Success<T>(val data: T): Response<T>()
    data class Error(val message: String?): Response<Nothing>()
}