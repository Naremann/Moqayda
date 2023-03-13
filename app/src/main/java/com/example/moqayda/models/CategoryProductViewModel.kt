package com.example.moqayda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable