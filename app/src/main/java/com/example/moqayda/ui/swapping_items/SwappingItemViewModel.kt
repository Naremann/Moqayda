package com.example.moqayda.ui.swapping_items

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

class SwappingItemViewModel:BaseViewModel<Navigator>() {

    var itemName: String? = null
    var navigator: Navigator? = null
    private val firebaseInstance = FirebaseRepo()
    private val currentUser = Firebase.auth.currentUser




    fun navigateToAddPrivateProductFragment() {
        navigator?.navigateToAddPrivateProductFragment()
    }

    fun sendChatRequest() {
        viewModelScope.launch {
            firebaseInstance.setRequests(MessageRequest("",
                currentUser!!.email, false,
                "Hello I want Chat with u"))
        }
    }




}