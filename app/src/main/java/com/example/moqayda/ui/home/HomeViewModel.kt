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
import com.example.moqayda.NetworkHandler
import com.example.moqayda.NetworkHandlerImp
import com.example.moqayda.R
import com.example.moqayda.api.ApiInterface
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.database.MyDatabase
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.repo.category.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(val categoryRepository: CategoryRepository) : BaseViewModel<Navigator>() {
   // lateinit var categoryOfflineRepository: CategoryOfflineRepository
    /*lateinit var categoryRepository: CategoryRepository

    lateinit var categoryOnlineRepository: CategoryOnlineRepository
    lateinit var networkHandler: NetworkHandler*/

    var progressBarVisible = MutableLiveData<Boolean>()

    private val _categoryList = MutableLiveData<List<CategoryItem?>?>()
    val categoryList: MutableLiveData<List<CategoryItem?>?>
    get() = _categoryList

    private val _navigateToProductListFragment = MutableLiveData<CategoryItem?>()
    val navigateToProductListFragment: LiveData<CategoryItem?>
        get() = _navigateToProductListFragment


    private val data = listOf(
        CategoryItem(
            id = null,
            name = "Electronics",
            isActive = true,
            pathImage = null,
            categoryBackgroundColor = R.color.electronics_category_color,
            categoryProductViewModels =  null
        ),
        CategoryItem(
            id = null,
            name = "Furniture",
            isActive = true,
            pathImage =  null,
            categoryBackgroundColor = R.color.category_furniture_color,
            categoryProductViewModels = null
        ),
        CategoryItem(
             id = null,
            name = "Fashion",
            isActive = true,
            pathImage = null,
            categoryBackgroundColor = R.color.category_fashion_color,
            categoryProductViewModels = null
        ),
        CategoryItem(
            id = null,
            name = "Books",
            isActive = true,
            pathImage = null,
            categoryBackgroundColor = R.color.category_books_color,
            categoryProductViewModels = null
        ),
        CategoryItem(
            id = null,
            name = "Pets",
            isActive = true,
            pathImage = null,
            categoryBackgroundColor = R.color.category_pets_color,
            categoryProductViewModels = null
        ),

        CategoryItem(
            id = null,
            name = "Other",
            isActive = true,
            pathImage = null,
            categoryBackgroundColor = R.color.category_other_color,
            categoryProductViewModels = null
        )
    )

    private fun fetchCategoryList() {
        progressBarVisible.value=true

        viewModelScope.launch {
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
       // categoryOfflineRepository=CategoryOfflineRepositoryImp(MyDatabase.getInstance()!!)
       /* networkHandler=NetworkHandlerImp()
        categoryOnlineRepository=CategoryOnlineRepositoryImp(RetrofitBuilder.retrofitService)

        categoryRepository=CategoryRepositoryImp(networkHandler,categoryOnlineRepository,categoryOfflineRepository)
        */
//        _categoryList.postValue(data)
        fetchCategoryList()

    }

}