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
import kotlinx.coroutines.launch

class HomViewModel : BaseViewModel<Navigator>() {

    var progressBarVisible = MutableLiveData<Boolean>()
    var navigator : Navigator?=null
    private val _categoryList = MutableLiveData<List<CategoryItem>>()
    val categoryList: LiveData<List<CategoryItem>>
        get() = _categoryList

    private val _navigateToProductListFragment = MutableLiveData<CategoryItem?>()
    val navigateToProductListFragment: LiveData<CategoryItem?>
        get() = _navigateToProductListFragment


    private val data = listOf(
        CategoryItem(
            null,
            "Electronics",
            true,
            null,
            R.color.electronics_category_color,
            null
        ),
        CategoryItem(
            null,
            "Furniture",
            true,
            null,
            R.color.category_furniture_color,
            null
        ),
        CategoryItem(
            null,
            "Fashion",
            true,
            null,
            R.color.category_fashion_color,
            null
        ),
        CategoryItem(
            null,
            "Books",
            true,
            null,
            R.color.category_books_color,
            null
        ),
        CategoryItem(
            null,
            "Pets",
            true,
            null,
            R.color.category_pets_color,
            null
        ),

        CategoryItem(
            null,
            "Other",
            true,
            null,
            R.color.category_other_color,
            null
        )
    )

    private fun fetchCategoryList() {
        progressBarVisible.value=true

        viewModelScope.launch {

            val response = RetrofitBuilder.retrofitService.getAllCategories()
            progressBarVisible.value=false
            try {
                if (response.isSuccessful) {
                    _categoryList.postValue(response.body())

                }
            }catch (ex:Exception){
                Log.e("ex","error"+ex.localizedMessage)
            }
        }

    }

    fun onCategorySelected(categoryItem: CategoryItem) {
        navigator?.navigateToProductListFragment(categoryItem)
        //_navigateToProductListFragment.postValue(categoryItem)
    }

    fun onProductListNavigated() {
        _navigateToProductListFragment.postValue(null)
    }

    init {

//        _categoryList.postValue(data)
        fetchCategoryList()

    }

}