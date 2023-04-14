package com.example.moqayda.ui.swapping_items

import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.MessageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SwappingItemViewModel:BaseViewModel<Navigator>() {
    var itemName:String?=null
    var navigator:Navigator?=null
    private val firebaseDatabase = Firebase.database
    val email = "khph@test" //هنفترض ان الايميل ده بتاع الشخص اللي رفع البوست
    fun navigateToAddPrivateProductFragment(){
        navigator?.navigateToAddPrivateProductFragment()
    }

    fun sendChatRequest(){
        val currentUser = Firebase.auth.currentUser?.email?.split(".")?.get(0)
        val myRef = firebaseDatabase.getReference("Users//${email}//Requests//${currentUser}")
        myRef.setValue(MessageRequest(Firebase.auth.currentUser?.email!!,"Hello"))
    }
}