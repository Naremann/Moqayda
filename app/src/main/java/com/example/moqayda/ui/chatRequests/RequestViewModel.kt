package com.example.moqayda.ui.chatRequests

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.database.getUserFromFirestore
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.Data
import com.example.moqayda.models.Message
import com.example.moqayda.models.MessageRequest
import com.example.moqayda.models.Notification
import com.example.moqayda.models.UserBlockage
import com.example.moqayda.notification.Notifications
import com.example.moqayda.repo.FirebaseRepo
import com.example.moqayda.repo.product.Resource
import kotlinx.coroutines.launch

class RequestViewModel : BaseViewModel<Navigator>() {

    lateinit var navigator: Navigator
    private val firebaseInstance = FirebaseRepo()

    val req = firebaseInstance.requestsLiveData
    val message = firebaseInstance.messagesLiveData

    private val _navigateToSelectedChat = MutableLiveData<String>()
    val navigateToSelectedChat: LiveData<String>
        get() = _navigateToSelectedChat

    val receiverUser = ObservableField<AppUser>()


    val userBlockageList = MutableLiveData<List<UserBlockage>>()

    private val _progressBarStatus = MutableLiveData<Boolean>()
    val progressBarStatus: LiveData<Boolean>
        get() = _progressBarStatus

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
        val notifications = Notifications()
        getUserFromFirestore(receiverUser.get()?.id!!, { docSnapshot ->
            val user = docSnapshot.toObject(AppUser::class.java)
            viewModelScope.launch {
                val result = notifications
                    .sendNotification(
                        Notification(
                            user?.token!!,
                            Data(
                                message.senderName!!,
                                message.text!!,
                                "chat"
                            )
                        )
                    )
                when (result) {
                    is Resource.Success<*> -> {
                        Log.e("TAG", "sendNotification: notification")
                    }
                    is Resource.Error<*> -> {
                        Log.e("TAG", "sendNotification: ${result.message}")
                    }
                }
            }

        }, {
            Log.e("SwapPublicItemViewModel", "Fail ${it.localizedMessage}")
        })

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

    suspend fun getUser(id: String) : AppUser? {
        return try {
            val result = RetrofitBuilder.retrofitService.getUserById(id)
            if (result.isSuccessful) {
                val user = result.body()
                user?.firstName?.let { Log.e("RequestViewModel", it) }
                user
            } else {
                result.message().let { Log.e("RequestViewModel", it) }
                null
            }
        } catch (e: Exception) {
            Log.e("RequestViewModel", e.message ?: "Unknown error")
            null
        }
    }

    init {
        getUsersList()
        getReq()
        Log.e("SwappingItemViewModel", "viewModelInit")
        getBlockedUsers()
    }




    private fun getBlockedUsers() {
        _progressBarStatus.postValue(true)
        viewModelScope.launch {
            val blockedUsersResponse = RetrofitBuilder.retrofitService.getBlockedUsers()
            if (blockedUsersResponse.isSuccessful) {
                userBlockageList.postValue(blockedUsersResponse.body())
            } else {
                Log.e(
                    "BlockedUsersViewModel",
                    "Failed to load blockList ${blockedUsersResponse.message()}"
                )
            }
        _progressBarStatus.postValue(false)
        }
    }





    fun navigateToUserProfile(user: AppUser){
        navigator.onNavigateToUserProfile(user)
    }


}