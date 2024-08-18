package com.kproject.toplightning.commom

sealed interface ResultState<out T> {
    data object Loading : ResultState<Nothing>
    data class Success<T>(val data: T) : ResultState<T>
    data class Error(val exception: Throwable) : ResultState<Nothing>
}