package com.example.moqayda.models

data class MessageRequest(
    var id: String? = "",
    val senderName: String? = "",
    var isApproved: Boolean? = false,
    var messageBody: String? = "",
)
