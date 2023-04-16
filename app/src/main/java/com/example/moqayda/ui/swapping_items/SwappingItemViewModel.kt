package com.example.moqayda.ui.swapping_items

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.MessageRequest
import com.example.moqayda.repo.FirebaseRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SwappingItemViewModel:BaseViewModel<Navigator>() {

    var itemName: String? = null
    var navigator: Navigator? = null
    private val firebaseInstance = FirebaseRepo()
    private val currentUser = Firebase.auth.currentUser
    private val database = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("Users")
    private var senderUserName = MutableLiveData("")
    private var receiverUserName = MutableLiveData("")
    private var receiverId = "keZnfdjMZ1NtTnMWURfYvtMUHrW2"

    init {
        getSenderName()
        getReceiverName()
    }
    fun navigateToAddPrivateProductFragment() {
        navigator?.navigateToAddPrivateProductFragment()
    }
    fun sendChatRequest() {
        if (!senderUserName.value.isNullOrBlank()) {
            Log.e("SwappingItemViewModel", "sender username is ${senderUserName.value}")
            Log.e("SwappingItemViewModel", "receiver username is ${receiverUserName.value}")
            viewModelScope.launch {
                firebaseInstance.setRequests(MessageRequest(
                    "",
                    senderId = currentUser?.uid,
                    senderName = senderUserName.value,
                    receiverId,
                    receiverName = receiverUserName.value,
                    false,
                    "Hello I want Chat with you"))
            }
        }

    }

    private fun getSenderName() {
        val userId = currentUser!!.uid
        val userRef: DatabaseReference = usersRef.child(userId)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val firstName = snapshot.child("firstName").getValue(String::class.java)
                    val lastName = snapshot.child("lastName").getValue(String::class.java)
                    senderUserName.postValue("$firstName $lastName")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("SwappingItemViewModel", "Error retrieving data: ${error.message}")
            }
        })
    }

    private fun getReceiverName(){
        val userId = receiverId
        val userRef: DatabaseReference = usersRef.child(userId)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val firstName = snapshot.child("firstName").getValue(String::class.java)
                    val lastName = snapshot.child("lastName").getValue(String::class.java)
                    receiverUserName.postValue("$firstName $lastName")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("SwappingItemViewModel", "Error retrieving data: ${error.message}")
            }
        })
    }
}