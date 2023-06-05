package com.example.moqayda.ui.swap_public_item_request

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.database.getUserFromFirestore
import com.example.moqayda.models.*
import com.example.moqayda.notification.Notifications
import com.example.moqayda.repo.FirebaseRepo
import com.example.moqayda.repo.product.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


class SwapPublicItemViewModel(ctx: Context) : BaseViewModel<Navigator>() {
    private val ctxReference: WeakReference<Context> = WeakReference(ctx)
    lateinit var navigator: Navigator
    var senderProduct = ObservableField<Product>()
    var receiverProduct = ObservableField<Product>()
    var productOwnerId: Int? = null

    private val _senderUser = Firebase.auth.currentUser
    private val firebaseInstance = FirebaseRepo()

    private val receiverUser = ObservableField<AppUser>()


    fun sendSwapRequestOfPublicItem() {
        showLoading.value = true
        val swapPublicItem = SwapPublicItem(
            id = 0,
            productId = receiverProduct.get()?.id!!,
            userId = receiverProduct.get()?.userId!!,
            productOwnerId = productOwnerId!!
        )
        viewModelScope.launch {
            Log.e(
                "sendRequestToSwap",
                "productId ${receiverProduct.get()?.id}  requestSenderProduct $productOwnerId"
            )

            val response = retrofitService.sendSwapRequestOfPublicItem(swapPublicItem)
            showLoading.value = false
            try {
                if (response.isSuccessful) {
                    sendChatRequest()
                    pushNotification()
                    Log.e("sendRequestToSwap()", "Success")
                    messageLiveData.postValue(ctxReference.get()?.getString(R.string.request_sent))
                    navigateToHomeFragment()
                } else {
                    messageLiveData.postValue(
                        ctxReference.get()?.getString(R.string.failed_to_send_request)
                    )

                    Log.e("sendRequestToSwap()", "Error")
                }
            } catch (ex: Exception) {
                messageLiveData.value = "Error, ${ex.localizedMessage}"
                Log.e("sendRequestToSwap()", "Fail ${ex.localizedMessage}")
            }

        }
    }

    fun getReceiver() {
        viewModelScope.launch {
            val userResponse = retrofitService.getUserById(receiverProduct.get()?.userId)
            if (userResponse.isSuccessful) {
                Log.e("SwapPublicItemViewModel", "User loaded successfully")
                receiverUser.set(userResponse.body())
            } else {
                Log.e("SwapPublicItemViewModel", "cannot load user")
            }
        }
    }

    private fun pushNotification() {
        val fragmentId = R.id.swapOffersOfPublicItemsFragment
        val notifications = Notifications()
        getReceiver()
        getUserFromFirestore(receiverProduct.get()?.userId!!, { docSnapshot ->
            val user = docSnapshot.toObject(AppUser::class.java)
            viewModelScope.launch {
                val result = notifications
                    .sendNotification(
                        Notification(
                            user?.token!!,
                            Data(
                                ctxReference.get()?.getString(R.string.noti_title)!!,
                                ctxReference.get()?.getString(R.string.noti_message)!!,
                                "swapOffer"
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


    private suspend fun sendChatRequest() {

        Log.e("SwapPublicItemViewModel",receiverProduct.get()?.name!!)
        Log.e("SwapPublicItemViewModel",receiverProduct.get()?.id.toString())
        firebaseInstance.setRequests(
            MessageRequest(
                "",
                senderId = _senderUser?.uid,
                senderName = _senderUser?.displayName,
                receiverId = receiverUser.get()?.id,
                receiverName = "${receiverUser.get()?.firstName} ${receiverUser.get()?.lastName}",
                false,
                messageBody = receiverProduct.get()?.name!!
            )
        )


    }

    fun navigateToHomeFragment() {
        navigator.onNavigateToHomeFragment()
    }
}