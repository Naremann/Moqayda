package com.example.moqayda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    val categoryImage: CategoryImage?,
    val id: Int?,
    val isActive: Boolean,
    val name: String,
    val image: Int,
    val categoryColor: Int
) : Parcelable