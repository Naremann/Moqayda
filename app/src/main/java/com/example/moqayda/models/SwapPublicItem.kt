package com.example.moqayda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SwapPublicItem (
    var id : Int?=null,
    var productId: Int,
    var userId: String,
    var productOwnerId: Int
    ) : Parcelable