package com.example.moqayda.models

data class BarteredProduct(
    val id: Int,
    val productId: Int,
    val userId: String,
    val productOwnerId: Int
)