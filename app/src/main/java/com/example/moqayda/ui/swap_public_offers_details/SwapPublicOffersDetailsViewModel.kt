package com.example.moqayda.ui.swap_public_offers_details

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.database.getUserFromFirestore
import com.example.moqayda.models.*
import com.example.moqayda.notification.Notifications
import com.example.moqayda.repo.product.ProductRepository
import com.example.moqayda.repo.product.Resource
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class SwapPublicOffersDetailsViewModel(ctx: Context) : BaseViewModel<Navigator>() {
    private val ctxReference: WeakReference<Context> = WeakReference(ctx)
    private val productRepository = ProductRepository(ctx)
    lateinit var navigator: Navigator
    private val _receiverProduct = MutableLiveData<Product>()
    val receiverProduct: LiveData<Product>
        get() = _receiverProduct

    private val _senderProduct = MutableLiveData<Product>()
    val senderProduct: LiveData<Product>
        get() = _senderProduct

    private val _senderUser = MutableLiveData<AppUser>()
    val senderUser: LiveData<AppUser>
        get() = _senderUser

    var user : AppUser?=null


    val productOwnerID = ObservableField<Int>()


    private val _progressBareStatus = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean>
        get() = _progressBareStatus

    fun getReceiverItemDetails(productId: Int) {
        viewModelScope.launch {
            val response = RetrofitBuilder.retrofitService.getProductById(productId)
            if (response.isSuccessful) {
                _receiverProduct.postValue(response.body())
            } else {
                Log.e("SPublicOffersDetailsVM", "Error: ${response.message()}")
            }
        }
    }

    init {
        getUserById(DataUtils.USER?.id!!)
    }


    fun getSenderItemDetails(productId: Int) {
        viewModelScope.launch {
            val response = RetrofitBuilder.retrofitService.getProductById(productId)
            if (response.isSuccessful) {
                _senderProduct.postValue(response.body())
                val result = RetrofitBuilder.retrofitService.getUserById(response.body()?.userId)
                if (result.isSuccessful) {
                    _senderUser.postValue(result.body())
                } else {
                    Log.e("SPublicOffersDetailsVM", "Failed to fetch User: ${result.message()}")
                }
            } else {
                Log.e("SPublicOffersDetailsVM", "Failed to fetch product: ${response.message()}")
            }
        }

    }

    fun addBarteredProducts() {

        val swapOffer = SwapPublicItem(
            productId = receiverProduct.value?.id!!,
            userId = receiverProduct.value?.userId!!,
            productOwnerId = productOwnerID.get()!!)
        val builder = AlertDialog.Builder(ctxReference.get()!!)
        builder.setTitle(ctxReference.get()!!.getString(R.string.confirmation))
        builder.setMessage(ctxReference.get()!!.getString(R.string.accept_swap_offer_confirmation))
        Log.e("SPublicOffersDetailsVM", senderProduct.value?.isActive.toString())
        builder.setPositiveButton(ctxReference.get()!!.getString(R.string.ok)) { _, _ ->
            _progressBareStatus.postValue(true)
            viewModelScope.launch {
                val response = RetrofitBuilder.retrofitService.addPublicBarteredProduct(swapOffer)
                if (response.isSuccessful) {
                    _progressBareStatus.postValue(false)
                    Log.e("SPublicOffersDetailsVM", "Success")
                    messageLiveData.postValue(ctxReference.get()!!.getString(R.string.swap_done))
                    deleteSelectedOffer(
                        receiverProduct.value?.userId!!,
                        receiverProduct.value!!.id!!,
                        productOwnerID.get()!!)
                    makeProductUnAvailable(receiverProduct.value!!)
                    makeProductUnAvailable(senderProduct.value!!)
                    navigateToProfileFragment()
                } else { messageLiveData.postValue(ctxReference.get()!!.getString(R.string.failure_message)) }
            }
        }
        builder.setNegativeButton(ctxReference.get()!!.getString(R.string.cancel)) { _, _ ->
            // Cancel button clicked
        }
        _progressBareStatus.postValue(false)
        val dialog = builder.create()
        dialog.show()
    }


    private suspend fun deleteOffer(offerId: Int) {
        _progressBareStatus.postValue(true)
        val response = RetrofitBuilder.retrofitService.deletePublicOffer(offerId)
        if (response.isSuccessful) {
            _progressBareStatus.postValue(false)
            Log.e("SPublicOffersDetailsVM", "offerDeletedSuccessfully")
        } else {
            Log.e("SPublicOffersDetailsVM", "failedToDeleteOffer: ${response.message()}")
        }
        _progressBareStatus.postValue(false)
    }

    private suspend fun deleteSelectedOffer(userId: String, productId: Int, productOwnerId: Int) {
        val response = RetrofitBuilder.retrofitService.getSwapPublicOffersBuUserId(userId)

        if (response.isSuccessful) {
            Log.e("SPublicOffersDetailsVM", "offers loaded successfully")
            response.body()?.userProdOffersViewModels?.forEach { productOffer ->
                if (productOffer?.productId == productId && productOffer.productOwnerId == productOwnerId) {
                    Log.e("SPublicOffersDetailsVM", "offer founded successfully")
                    deleteOffer(productOffer.id!!)
                } else {
                    Log.e("SPublicOffersDetailsVM", "cannot found offer")
                }
            }
        } else {
            Log.e("SPublicOffersDetailsVM", "failedToLoadOffers: ${response.message()}")
        }
    }

    private suspend fun makeProductUnAvailable(product: Product) {
        val result = productRepository.updateProductWithCurrentImage(
            product.id.toString(),
            product.name!!,
            product.descriptions!!,
            "false",
            product.categoryId.toString(),
            "0",
            product.productToSwap!!,
            product.pathImage
        )
        when (result) {
            is Resource.Success<*> -> {
                Log.e("SPublicOffersDetailsVM", "Product updated successfully")

            }

            is Resource.Error<*> -> {
                Log.e("SPublicOffersDetailsVM", "error: ${result.message}")

            }
        }
    }


    fun deleteTheOffer() {
        val builder = AlertDialog.Builder(ctxReference.get()!!)
        builder.setTitle(ctxReference.get()!!.getString(R.string.confirmation))

        builder.setMessage(
            ctxReference.get()!!.getString(R.string.delete_swap_offer_confirmation)
        )
        Log.e("SPublicOffersDetailsVM", senderProduct.value?.isActive.toString())
        builder.setPositiveButton(ctxReference.get()!!.getString(R.string.ok)) { _, _ ->
            viewModelScope.launch {
                val response =
                    RetrofitBuilder.retrofitService.getSwapPublicOffersBuUserId(_receiverProduct.value?.userId)
                if (response.isSuccessful) {
                    Log.e("SPublicOffersDetailsVM", "offers loaded successfully")
                    response.body()?.userProdOffersViewModels?.forEach { productOffer ->
                        if (productOffer?.productId == _receiverProduct.value?.id && productOffer?.productOwnerId == productOwnerID.get()) {
                            Log.e("SPublicOffersDetailsVM", "offer founded successfully")
                            val result =
                                RetrofitBuilder.retrofitService.deletePublicOffer(productOffer?.id!!)
                            if (result.isSuccessful) {
                                pushNotification(senderProduct.value?.userId,"${user?.firstName}"+" "+
                                    "${user?.lastName}"+"is rejected ${senderProduct.value?.name} offer")
                                Log.e("Noti", "${receiverProduct.value?.userId}")
                                Log.e("Notifi", "${_receiverProduct.value?.userId}")
                                Log.e("SPublicOffersDetailsVM", "offerDeletedSuccessfully")
                                messageLiveData.postValue(
                                    ctxReference.get()
                                        ?.getString(R.string.offer_deleted_successfully)
                                )
                                navigateToProfileFragment()
                            } else {
                                Log.e(
                                    "SPublicOffersDetailsVM",
                                    "failedToDeleteOffer: ${result.message()}"
                                )
                                messageLiveData.postValue(
                                    ctxReference.get()?.getString(R.string.failure_message)
                                )
                            }
                        } else {
                            Log.e("SPublicOffersDetailsVM", "cannot found offer")
                        }
                    }
                } else {
                    Log.e("SPublicOffersDetailsVM", "failedToLoadOffers: ${response.message()}")
                }
            }
        }
        builder.setNegativeButton(ctxReference.get()!!.getString(R.string.cancel)) { _, _ ->
            // Cancel button clicked
        }
        val dialog = builder.create()
        dialog.show()

    }

    private fun pushNotification(senderUserId:String?, notiMessage:String) {
        val notifications = Notifications()
        if (senderUserId != null) {
            getUserById(senderUserId)
        }
        if (senderUserId != null) {
            getUserFromFirestore(senderUserId, { docSnapshot ->
                val user = docSnapshot.toObject(AppUser::class.java)
                viewModelScope.launch {
                    val result = notifications
                        .sendNotification(
                            Notification(
                                user?.token!!,
                                Data(
                                    ctxReference.get()?.getString(R.string.rejected_swap)!!,
                                    notiMessage,
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

    }
    private fun getUserById(userId:String) {
        viewModelScope.launch {
            val userResponse = RetrofitBuilder.retrofitService.getUserById(userId)
            if (userResponse.isSuccessful) {
                user=userResponse.body()
                Log.e("SwapPublicItemViewModel", "User loaded successfully")
            } else {
                Log.e("SwapPublicItemViewModel", "cannot load user")
            }
        }
    }






    private fun navigateToProfileFragment() {
        navigator.onNavigateToProfileFragment()
    }

    fun navigateToChatRequestsFragment() {
        navigator.onNavigateToChatRequestsFragment()
    }
}


