package com.example.moqayda.ui.user_public_items

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.Product
import com.example.moqayda.models.ProductOwnerItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class UserPublicItemViewModel( ctx: Context) :BaseViewModel<Navigator>() {
    private val ctxReference: WeakReference<Context> = WeakReference(ctx)

    lateinit var navigator: Navigator
    var isVisibleProgress = MutableLiveData<Boolean>()
    var product = MutableLiveData<List<Product?>?>()
    var privateProduct:Product?=null
    var productOwnerItemId=MutableLiveData<Int>()

    init {
        fetchUserPublicItems()

    }

     fun addProductOwner(privateProductId:Int){
        Log.e("addProductOwner","privateProduct?.id!! $privateProduct?.id!!")
        val userId = DataUtils.USER?.id
        val productOwnerItem=ProductOwnerItem(0,privateProductId,userId!!)
        viewModelScope.launch {
            val response = retrofitService.addProductOwner(productOwnerItem)
            try {
                if(response.isSuccessful){
                    productOwnerItemId.value=productOwnerItem.id
                    Log.e("addProductOwner","Success")
                }
            }catch (ex:Exception){
                Log.e("addProductOwner","Fail ${ex.localizedMessage}")
            }
        }
    }



    private fun fetchUserPublicItems(){
        isVisibleProgress.value=true
        val userId = DataUtils.USER?.id
        viewModelScope.launch {
           val result =  retrofitService.getUserById(userId).body()?.userProductViewModels
            isVisibleProgress.value=false
            try {
                product.value=result
                Log.e("success","response$result")

            }
            catch (ex:Exception){
                messageLiveData.value=ex.localizedMessage
                Log.e("ex","error"+ex.localizedMessage)

            }

        }
    }

    fun deleteSelectedProduct(product: Product){
        viewModelScope.launch { 
            if (product.id != null && product.userId == Firebase.auth.currentUser!!.uid){
                val response = retrofitService.deleteProduct(product.id)
                
                if (response.isSuccessful){
                    messageLiveData.postValue(ctxReference.get()?.getString(R.string.post_deleted_successfully))

                }else{
                    Log.e("UserPublicItemViewModel","Failed to delete the product ${response.message()}")
                    Log.e("UserPublicItemViewModel","${product.id}")
                    Log.e("UserPublicItemViewModel","${response.body()}")
                    messageLiveData.postValue(ctxReference.get()?.getString(R.string.filed_to_delete_post))
                }
                
            }else{
                Log.e("UserPublicItemViewModel","failed to recognize user")

            }
            
        }
    }




    fun navigateToProductDetails(product: Product){
        navigator.onNavigateToProductDetails(product)
    }

    fun updateSelectedProduct(product: Product){
        
    }
    
    

}