package com.example.moqayda.models

import android.os.Parcelable
import com.example.moqayda.ui.products.ProductViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppUser(
    val id: String? = null,
    val firstName:String?=null,
    val lastName:String?=null,
    val phoneNumber: String? =null,
    val country:String?=null,
    val city:String?=null,
    val address:String?=null,
    @SerializedName("email")
    val email:String?=null,
    @SerializedName("pathImage")
    val image:String?=null,
    @SerializedName("userProductViewModels")
    val userProductViewModels:List<Product?>?=null,
    val token:String?=null

        ) : Parcelable {
    companion object{
        const val COLLECTION_NAME="user"
    }
}