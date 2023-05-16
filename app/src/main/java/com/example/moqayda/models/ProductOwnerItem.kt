package com.example.moqayda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductOwnerItem(
    var id: Int,
    var productId: Int,
    var userId: String,
) : Parcelable