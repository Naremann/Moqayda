package com.example.moqayda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductOwner(
    val id: Int,
    val productId: Int,
    val userId: String
) : Parcelable