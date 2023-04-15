package com.example.moqayda.repo

import com.example.moqayda.models.AppUser
import com.example.moqayda.models.Message
import com.example.moqayda.models.MessageRequest

interface FirebaseRepoInterface {
    suspend fun getUsers(): List<AppUser>
    suspend fun getRequests(): List<MessageRequest>
    suspend fun setRequests(request: MessageRequest): Boolean
    suspend fun openReq(request:MessageRequest): Boolean
    suspend fun deleteReq(request:MessageRequest): Boolean
    suspend fun getMessage(reqId: String): List<Message>
    suspend fun setMessage(message: Message,reqId:String): Boolean
}