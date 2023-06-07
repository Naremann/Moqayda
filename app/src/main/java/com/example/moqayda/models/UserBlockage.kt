package com.example.moqayda.models

data class UserBlockage(
    val id: Int?=null,
    val blockingUserId: String,
    val blockedUserId: String
)