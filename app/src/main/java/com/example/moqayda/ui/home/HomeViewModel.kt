//package com.example.moqayda.ui.home
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.moqayda.R
//import com.example.moqayda.api.RetrofitBuilder
//import com.example.moqayda.models.CategoryItem
//import kotlinx.coroutines.launch
//
//class HomViewModel : ViewModel() {
//
//
//    private val _categoryList = MutableLiveData<List<CategoryItem>>()
//    val categoryList: LiveData<List<CategoryItem>>
//        get() = _categoryList
//
//    private val _navigateToProductDetails = MutableLiveData<CategoryItem?>()
//    val navigateToProductDetails: LiveData<CategoryItem?>
//        get() = _navigateToProductDetails
//
//    init {
//
//        fetchCategoryList()
//    }
//
//
//    private fun fetchCategoryList() {
//
//        viewModelScope.launch {
//            val response = RetrofitBuilder.retrofitService.getAllCategories()
//            if (response.isSuccessful) {
//                _categoryList.postValue(response.body())
//            }
//        }
//
//    }
//    fun onCategoryListenerClicked(categoryItem: CategoryItem){
//        _navigateToProductDetails.value = categoryItem
//    }
//
//
//
//}

package com.example.moqayda.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.repo.category.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(val categoryRepository: CategoryRepository) : BaseViewModel<Navigator>() {
    lateinit var categoryOnlineRepository: CategoryOnlineRepository
    // lateinit var categoryOfflineRepository: CategoryOfflineRepository
    /*lateinit var categoryRepository: CategoryRepository


    lateinit var networkHandler: NetworkHandler*/

    var progressBarVisible = MutableLiveData<Boolean>()

    private val _categoryList = MutableLiveData<List<CategoryItem?>?>()
    val categoryList: MutableLiveData<List<CategoryItem?>?>
    get() = _categoryList

    private val _navigateToProductListFragment = MutableLiveData<CategoryItem?>()
    val navigateToProductListFragment: LiveData<CategoryItem?>
        get() = _navigateToProductListFragment


    private fun fetchCategoryList() {
        progressBarVisible.value=true

        viewModelScope.launch {
            //val result = categoryRepository.getCategories()
            val result = categoryRepository.getCategories()
            progressBarVisible.value=false
            try {
                //if (response.isSuccessful) {
                    _categoryList.postValue(result)
                Log.e("success","response$result")
               // }
            }catch (ex:Exception){
                messageLiveData.value=ex.localizedMessage
                Log.e("ex","error"+ex.localizedMessage)
            }
        }

    }

    fun onCategorySelected(categoryItem: CategoryItem) {
        _navigateToProductListFragment.postValue(categoryItem)
    }

    fun onProductListNavigated() {
        _navigateToProductListFragment.postValue(null)
    }

    init {
        categoryOnlineRepository=CategoryOnlineRepositoryImp(RetrofitBuilder.retrofitService)
       // categoryOfflineRepository=CategoryOfflineRepositoryImp(MyDatabase.getInstance()!!)
       /* networkHandler=NetworkHandlerImp()


        categoryRepository=CategoryRepositoryImp(networkHandler,categoryOnlineRepository,categoryOfflineRepository)
        */
//        _categoryList.postValue(data)
        fetchCategoryList()

    }

}