package com.uphar.bussinesss.domain.Utils.state

import java.lang.Exception

sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error<out E>(val exception: Exception) : DataState<E>()
    data class Loading<out B>(val loading: Boolean) : DataState<B>()
}