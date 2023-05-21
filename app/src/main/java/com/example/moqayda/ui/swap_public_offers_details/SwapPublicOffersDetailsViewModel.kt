package com.example.moqayda.ui.swap_public_offers_details

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.Product
import com.example.moqayda.models.SwapPublicItem
import com.example.moqayda.repo.product.ProductRepository
import com.example.moqayda.repo.product.Resource
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class SwapPublicOffersDetailsViewModel(ctx: Context) :BaseViewModel<Navigator>() {
    private val ctxReference: WeakReference<Context> = WeakReference(ctx)
    val productRepository = ProductRepository(ctx)
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

    val productOwnerID = ObservableField<Int>()

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
        val swapOffer = SwapPublicItem(productId = receiverProduct.value?.id!!,
            userId = receiverProduct.value?.userId!!, productOwnerId = productOwnerID.get()!!)

        val builder = AlertDialog.Builder(ctxReference.get()!!)
        builder.setTitle(ctxReference.get()!!.getString(R.string.confirmation))

        builder.setMessage(ctxReference.get()!!
            .getString(R.string.accept_swap_offer_confirmation))

        builder.setPositiveButton(ctxReference.get()!!.getString(R.string.ok)) {
                dialog, which ->
            viewModelScope.launch {
                val response = RetrofitBuilder.retrofitService.addPublicBarteredProduct(swapOffer)
                if (response.isSuccessful) {
                    Log.e("SPublicOffersDetailsVM", "Success")
                    messageLiveData.postValue(ctxReference.get()!!.getString(R.string.swap_done))

                    deleteSelectedOffer(receiverProduct.value?.userId!!,
                        receiverProduct.value!!.id!!,
                        productOwnerID.get()!!)
                    makeProductUnAvailable(receiverProduct.value!!)
                    makeProductUnAvailable(senderProduct.value!!)
                    navigateToSwapPublicOffers()
                }else{
                    Log.e("SPublicOffersDetailsVM","Failed : ${response.message()}")
                    messageLiveData.postValue(ctxReference.get()!!.getString(R.string.filure_message))
                }
            }
        }

        builder.setNegativeButton(ctxReference.get()!!.getString(R.string.cancel)) { _, _ ->
            // Cancel button clicked
        }
        val dialog = builder.create()
        dialog.show()


    }

    private fun navigateToSwapPublicOffers() {
        navigator.onNavigateToPublicSwapOffersFragment()
    }


    private suspend fun deleteOffer(offerId: Int) {

        val response = RetrofitBuilder.retrofitService.deletePublicOffer(offerId)
        if (response.isSuccessful) {
            Log.e("SPublicOffersDetailsVM", "offerDeletedSuccessfully")
        } else {
            Log.e("SPublicOffersDetailsVM", "failedToDeleteOffer: ${response.message()}")
        }

    }


    private suspend fun deleteSelectedOffer(userId: String, productId: Int, productOwnerId: Int) {
        val response = RetrofitBuilder.retrofitService.getSwapPublicOffersBuUserId(userId)
        if (response.isSuccessful) {
            Log.e("SPublicOffersDetailsVM", "offers loaded successfully")
            response.body()?.userProdOffersViewModels?.forEach { productOffer ->
                if (productOffer?.productId == productId && productOffer.productOwnerId == productOwnerId) {
                    Log.e("SPublicOffersDetailsVM", "offer founded successfully")
                    deleteOffer(productOffer.id!!)
                }else{
                    Log.e("SPublicOffersDetailsVM", "cannot found offer")
                }
            }
        } else {
            Log.e("SPublicOffersDetailsVM", "failedToLoadOffers: ${response.message()}")
        }
    }

    private suspend fun makeProductUnAvailable(product: Product){
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

}