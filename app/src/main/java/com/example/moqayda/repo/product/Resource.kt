package com.example.moqayda.repo.product

import android.net.Uri

sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null,
        ){
    class Error<T>(errorMessage: String) : Resource<T>(message = errorMessage)
    class Success<T>(data: T): Resource<T>(data = data)
}