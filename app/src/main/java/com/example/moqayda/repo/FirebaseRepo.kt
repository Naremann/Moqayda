package com.example.moqayda.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.Message
import com.example.moqayda.models.MessageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRepo : FirebaseRepoInterface {
    private val firebaseDatabase = Firebase.database
    private val tag = "FirebaseRepo"
    var usersLiveData = MutableLiveData<List<AppUser>>()
    var requestsLiveData = MutableLiveData<List<MessageRequest>>()
    var messagesLiveData = MutableLiveData<List<Message>>()

    private val currentUser = Firebase.auth.currentUser
    private val receiver = "u0TWeU4HTCYYEjUgfW3gMlMHZkV2"//-> user l product

    override suspend fun getUsers(): List<AppUser> {
        val myRef = firebaseDatabase.getReference("Users")
        val usersList = ArrayList<AppUser>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                for (postSnapshot in snapshot.children) {
                    val user: AppUser = postSnapshot.getValue(AppUser::class.java)!!
                    usersList.add(user)
                    Log.d(tag, "Users loaded  Successfully with id ${user.id}")
                }
                usersLiveData.value = usersList
                Log.d(tag, "Users size ${usersList.size}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(tag, "load User:onCancelled", databaseError.toException())
            }
        })
        return usersList
    }

    override suspend fun getRequests(): List<MessageRequest> {
        val myRef = firebaseDatabase.getReference("Users//${currentUser!!.uid}//requests")
        val requestsList = ArrayList<MessageRequest>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                requestsList.clear()
                for (postSnapshot in snapshot.children) {
                    val request: MessageRequest = postSnapshot.getValue(MessageRequest::class.java)!!
                    requestsList.add(request)
                    Log.d(tag, "request load Successfully with id ${request.id}")
                }
                requestsLiveData.value = requestsList
                Log.d(tag, "requests size ${requestsList.size}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(tag, "load Request:onCancelled", databaseError.toException())
            }
        })
        return requestsList
    }

    override suspend fun setRequests(request: MessageRequest): Boolean {
        var isUploaded = false
        var myRef = firebaseDatabase.getReference("Users//${request.receiverId}//requests").push()
        request.id = myRef.key.toString()
        myRef.setValue(request).addOnSuccessListener {
            Log.d(tag, "Upload Successfully id -> ${myRef.key}")
            isUploaded = true
        }.addOnFailureListener {
            Log.d(tag, "Upload Failure")
        }
        myRef = firebaseDatabase.getReference("Users//${currentUser!!.uid}//requests//${request.id}")
        request.isApproved = true
        myRef.setValue(request).addOnSuccessListener {
            Log.d(tag, "Upload Successfully id -> ${myRef.key}")
            isUploaded = true
        }.addOnFailureListener {
            Log.d(tag, "Upload Failure")
        }
        return isUploaded
    }

    override suspend fun openReq(request: MessageRequest): Boolean {
        var isUploaded = false
        request.isApproved = true
        val myRef = firebaseDatabase.getReference("Users//${currentUser!!.uid}//requests//${request.id}")
        myRef.setValue(request).addOnSuccessListener {
            Log.d(tag, "approved Successfully id -> ${request.id}")
            isUploaded = true
        }.addOnFailureListener {
            Log.d(tag, "approved Failure")
        }
        return isUploaded
    }

    override suspend fun deleteReq(request: MessageRequest): Boolean {
        var isDeleted = false
        val myRef = firebaseDatabase.getReference("Users//${currentUser!!.uid}//requests//${request.id}")
        myRef.removeValue().addOnSuccessListener {
            Log.d(tag, "deleted Successfully id -> ${request.id}")
            isDeleted = true
        }.addOnFailureListener {
            Log.d(tag, "deleted Failure")
        }
        return isDeleted
    }

    override suspend fun getMessage(reqId: String): List<Message> {
        val myRef = firebaseDatabase.getReference("messages//${reqId}")
        val messagesList = ArrayList<Message>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                for (postSnapshot in snapshot.children) {
                    val message: Message = postSnapshot.getValue(Message::class.java)!!
                    messagesList.add(message)
                    Log.d(tag, "message loaded  Successfully with id ${message.id}")
                }
                messagesLiveData.value = messagesList
                android.util.Log.d(tag, "messagesList size ${messagesList.size}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(tag, "load Message :onCancelled", databaseError.toException())
            }
        })
        return messagesList
    }

    override suspend fun setMessage(message: Message, reqId: String): Boolean {
        var isUploaded = false
        val myRef = firebaseDatabase.getReference("messages//${reqId}").push()
        myRef.setValue(message).addOnSuccessListener {
            Log.d(tag, "Uploaded Successfully id -> $reqId")
            isUploaded = true
        }.addOnFailureListener {
            Log.d(tag, "Upload Failure")
        }
        return isUploaded
    }
}