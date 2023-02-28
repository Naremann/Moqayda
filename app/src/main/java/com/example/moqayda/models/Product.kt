package com.example.moqayda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val descriptions: String,
    val pathImage: String,
    val availableSince: String,
    val isActive: Boolean,
    val categoryId: Int
) : Parcelable