package com.example.moqayda.ui.chatRequests

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.Message
import com.example.moqayda.models.MessageRequest
import com.example.moqayda.repo.FirebaseRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class RequestViewModel : BaseViewModel<Navigator>() {

    private val firebaseInstance = FirebaseRepo()

    val req = firebaseInstance.requestsLiveData
    val message = firebaseInstance.messagesLiveData

    private val _navigateToSelectedChat = MutableLiveData<String>()
    val navigateToSelectedChat: LiveData<String>
        get() = _navigateToSelectedChat



    fun selectedChat(chat: String) {
        _navigateToSelectedChat.value = chat
    }

    fun openReq(request: MessageRequest) {
        viewModelScope.launch {
            firebaseInstance.openReq(request)
        }
    }

    fun deleteReq(request: MessageRequest) {
        viewModelScope.launch {
            firebaseInstance.deleteReq(request)
        }
    }

    fun setMessage(message: Message, reqID: String) {
        viewModelScope.launch {
            firebaseInstance.setMessage(message, reqID)

        }
    }

    fun getMessage(reqID: String) {
        viewModelScope.launch {
            firebaseInstance.getMessage(reqID)
        }
    }

    private fun getUsersList() {
        viewModelScope.launch {
            firebaseInstance.getUsers()

        }
    }

    private fun getReq() {
        viewModelScope.launch {
            firebaseInstance.getRequests()

        }
    }

    init {
        getUsersList()
        getReq()
        Log.e("SwappingItemViewModel", "viewModelInit")
    }


}