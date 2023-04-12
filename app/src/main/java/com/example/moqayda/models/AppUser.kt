package com.example.moqayda.models

data class AppUser(
    var id: String? = null,
    val firstName:String?=null,
    val lastName:String?=null,
    val phoneNumber: String? =null,
    val country:String?=null,
    val city:String?=null,
    val address:String?=null,
    val email:String?=null,
    val image:String?=null
        )
{
    companion object{
        const val COLLECTION_NAME="user"
    }
}