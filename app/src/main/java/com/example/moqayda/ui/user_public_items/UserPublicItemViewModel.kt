package com.example.moqayda.ui.user_public_items

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.PrivateProduct
import com.example.moqayda.models.ProductOwnerItem
import kotlinx.coroutines.launch

class UserPublicItemViewModel:BaseViewModel<Navigator>() {
    var isVisibleProgress = MutableLiveData<Boolean>()
    var product = MutableLiveData<List<PrivateProduct?>?>()
    var privateProduct:PrivateProduct?=null
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
}