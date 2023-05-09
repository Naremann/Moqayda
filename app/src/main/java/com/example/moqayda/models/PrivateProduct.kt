package com.example.moqayda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrivateProduct(
    val id: Int?=null,
    val name: String?=null,
    val descriptions: String?=null,
    val pathImage: String?=null,
    val userId: String?=null
) : Parcelable