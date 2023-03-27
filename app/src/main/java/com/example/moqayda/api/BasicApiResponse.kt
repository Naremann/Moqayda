package com.example.moqayda.api

data class BasicApiResponse<T>(
    val message: String? = null,
    val successful: Boolean,
    val data: T? = null
)