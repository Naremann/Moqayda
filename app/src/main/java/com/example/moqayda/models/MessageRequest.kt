package com.example.moqayda.models

data class MessageRequest(
    var id: String? = "",
    var senderId: String?="",
    val senderName: String? = "",
    var receiverId: String?="",
    val receiverName:String? = "",
    var isApproved: Boolean? = false,
    var messageBody: String? = "",
)
