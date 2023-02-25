package com.example.moqayda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    val id: Int?,
    val name: String,
    val isActive: Boolean,
    val pathImage: String?,
    val categoryBackgroundColor: Int,
    val categoryProductViewModels: List<Product>?
) : Parcelable