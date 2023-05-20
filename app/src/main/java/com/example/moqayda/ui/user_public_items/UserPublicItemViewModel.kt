package com.example.moqayda.ui.user_public_items

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.Product
import com.example.moqayda.models.ProductOwnerItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class UserPublicItemViewModel( ctx: Context) :BaseViewModel<Navigator>() {
    private val ctxReference: WeakReference<Context> = WeakReference(ctx)
    val userId = DataUtils.USER?.id
    lateinit var navigator: Navigator
    var isVisibleProgress = MutableLiveData<Boolean>()
    var product = MutableLiveData<List<Product?>?>()
    var senderProduct:Product?=null
    var productOwnerItemId=MutableLiveData<Int>()



    private val dataList = mutableListOf<Product>()




    init {
        fetchUserPublicItems()

    }

    /* fun addProductOwner(ProductId:Int){
         showLoading.value=true
        Log.e("addProductOwner","privateProduct?.id!! $senderProduct?.id!!")

        val productOwnerItem=ProductOwnerItem(0,ProductId,userId!!)
        viewModelScope.launch {
            val response = retrofitService.addProductOwner(productOwnerItem)
            showLoading.value=false
            try {
                if(response.isSuccessful){
                    productOwnerItemId.value=productOwnerItem.id
                    Log.e("addProductOwner","Success")
                }
            }catch (ex:Exception){
                Log.e("addProductOwner","Fail ${ex.localizedMessage}")
            }
        }
    }*/



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
            val selectedProductResponse = retrofitService.getProductById(product.id)
            if (selectedProductResponse.isSuccessful) {
                val selectedProduct = selectedProductResponse.body()
                Log.e("UserPublicItemViewModel","selected product owner ${selectedProduct?.productAndOwnerViewModels}")
                if (selectedProduct?.productAndOwnerViewModels?.isNotEmpty() == true){
                    selectedProduct.productAndOwnerViewModels.forEach {
                        Log.e("UserPublicItemViewModel", selectedProduct.productAndOwnerViewModels.toString())
                        val deleteProductOwnerResponse = retrofitService.deleteProductOwner(it?.id!!)
                        if (deleteProductOwnerResponse.isSuccessful) {
                            Log.e("UserPublicItemViewModel", "Product owner deleted successfully")
                        } else {
                            Log.e("UserPublicItemViewModel",
                                "Failed To delete product owner ${deleteProductOwnerResponse.message()}")
                        }
                    }
                }
                if (selectedProduct?.id != null && selectedProduct.userId == Firebase.auth.currentUser!!.uid) {
                    val response = retrofitService.deleteProduct(selectedProduct.id)
                    if (response.isSuccessful) {
                        messageLiveData.postValue(ctxReference.get()
                            ?.getString(R.string.post_deleted_successfully))
                        fetchUserPublicItems()
                    } else {
                        Log.e("UserPublicItemViewModel",
                            "Failed to delete the product ${response.message()}")
                        messageLiveData.postValue(ctxReference.get()?.getString(R.string.filed_to_delete_post))
                    }
                }else{
                    Log.e("UserPublicItemViewModel","failed to recognize user")
                }
            } else {
                Log.e("UserPublicItemViewModel",
                    "Failed To Load selectedProduct ${selectedProductResponse.message()}")
            }
        }
    }



    fun navigateToProductDetails(product: Product){
        navigator.onNavigateToProductDetails(product)
    }

    fun navigateToUpdateProduct(product: Product,categoryId: Int){
        navigator.onNavigateTOUpdateProductFragment(product,categoryId)
    }

    

}