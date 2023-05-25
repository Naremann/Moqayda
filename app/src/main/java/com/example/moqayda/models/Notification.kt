package com.example.moqayda.models

data class Notification (
    val to: String,
    val data: Data,
        )
data class Data(
    val title: String,
    val message: String,
    val actionName:String?=null
)
