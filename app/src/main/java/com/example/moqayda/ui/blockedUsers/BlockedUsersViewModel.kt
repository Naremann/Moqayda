package com.example.moqayda.ui.blockedUsers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.UserBlockage
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class BlockedUsersViewModel(mContext: Context):BaseViewModel<Navigator>(){

    private val ctxReference: WeakReference<Context> = WeakReference(mContext)
    val userBlockage = MutableLiveData<List<UserBlockage>>()
    private val blockedUsersList = mutableListOf<AppUser>()

    private val _blockedUsers = MutableLiveData<List<AppUser>>()
    val blockedUsers:LiveData<List<AppUser>>
        get() = _blockedUsers

    private val _isEmpty = MutableLiveData(false)
    val isEmpty:LiveData<Boolean>
        get() = _isEmpty

    private val _progressBarStatus = MutableLiveData<Boolean>()
    val progressBarStatus: LiveData<Boolean>
        get() = _progressBarStatus

    init {
        getBlockedUsers()
    }


    fun getBlockedUsers(){
        blockedUsersList.clear()
        _progressBarStatus.postValue(true)
        viewModelScope.launch {
            val blockedUsersResponse = RetrofitBuilder.retrofitService.getBlockedUsers()
            if (blockedUsersResponse.isSuccessful){
                userBlockage.postValue(blockedUsersResponse.body())
                blockedUsersResponse.body()?.forEach {
                    if (it.blockingUserId == DataUtils.USER?.id){
                        val userResponse = RetrofitBuilder.retrofitService.getUserById(it.blockedUserId)
                        if (userResponse.isSuccessful){
                            blockedUsersList.add(userResponse.body()!!)
                        }else{
                            Log.e("BlockedUsersViewModel","Failed to load User ${userResponse.message()}")
                        }
                    }
                }
            }else{
                Log.e("BlockedUsersViewModel","Failed to load blockList ${blockedUsersResponse.message()}")
            }
            _blockedUsers.postValue(blockedUsersList)
            _progressBarStatus.postValue(false)
            if (blockedUsersList.isEmpty()){
                _isEmpty.postValue(true)
            }
        }
    }

    fun unBlockUser(userId:String){
        _progressBarStatus.postValue(true)
        viewModelScope.launch {
            userBlockage.value?.forEach {
                if (it.blockedUserId == userId){
                    val response = RetrofitBuilder.retrofitService.unBlockUser(it.id!!)
                    if (response.isSuccessful){
                        messageLiveData.postValue(ctxReference.get()?.getString(R.string.user_unblocked))
                        getBlockedUsers()
                    }else{
                        Log.e("BlockedUsersViewModel","Failed to unblock user ${response.message()}")
                        messageLiveData.postValue(ctxReference.get()?.getString(R.string.failure_message))
                    }
                }
            }
            _progressBarStatus.postValue(false)
        }
    }

}