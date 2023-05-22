package com.example.moqayda.ui.swapping_items

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.MessageRequest
import com.example.moqayda.repo.FirebaseRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SwappingItemViewModel:BaseViewModel<Navigator>() {

    var itemName: String? = null
    var itemId: Int? = null
    var isCheckedPrivateBtn=MutableLiveData<Boolean>()
    var isCheckedPublicBtn=MutableLiveData<Boolean>()

    var navigator: Navigator? = null
    private val currentUser = Firebase.auth.currentUser
    private val database = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("Users")
    private var senderUserName = MutableLiveData("")
    private var receiverUserName = MutableLiveData("")
    var receiverId: String? = null

    private val _appUser = MutableLiveData<AppUser?>()
    val appUser: LiveData<AppUser?>
        get() = _appUser

    init {
        getSenderName()

    }

    fun navigateToUserPublicProductsFragment(){
        isCheckedPublicBtn.value=true
        navigator?.navigateToUserPublicProductsFragment()
    }
    fun navigateToAddPrivateProductFragment() {
        isCheckedPrivateBtn.value=true
        navigator?.navigateToPrivateProductFragment()
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

     fun getReceiverData(){
        val userId = receiverId
        val userRef: DatabaseReference = usersRef.child(userId!!)
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
        viewModelScope.launch {
            val response = RetrofitBuilder.retrofitService.getUserById(userId)
            if (response.isSuccessful){
                _appUser.postValue(response.body())

            }else{
                Log.e("SwappingItemViewModel",response.message())
            }
        }
    }
    
    
}