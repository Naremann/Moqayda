package com.example.moqayda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryImage(
    val base64Image: String,
    val categoryId: Int,
    val id: Int,
    val mime: String
) : Parcelable