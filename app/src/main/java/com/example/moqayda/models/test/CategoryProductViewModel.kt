package com.example.moqayda.models.test

data class CategoryProductViewModel(
    val availableSince: String,
    val categoryId: Int,
    val descriptions: String,
    val id: Int,
    val isActive: Boolean,
    val isFavourite: Boolean,
    val name: String,
    val pathImage: String,
    val productToSwap: String
)