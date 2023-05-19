package com.example.moqayda.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrivateProduct(
    val id: Int?=null,
    val name: String?=null,
    val descriptions: String?=null,
    val pathImage: String?=null,
    val userId: String?=null
) : Parcelable
class PrivateItemsResponse:ArrayList<PrivateProduct>()
data class PrivateResponse(

    @field:SerializedName("PrivateResponse")
    val privateResponse: List<PrivateProduct?>? = null
)
