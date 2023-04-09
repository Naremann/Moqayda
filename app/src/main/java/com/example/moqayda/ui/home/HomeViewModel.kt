package com.example.moqayda.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.repo.category.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(private val categoryRepository: CategoryRepository) : BaseViewModel<Navigator>() {
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
            val result = categoryRepository.getCategories()
            progressBarVisible.value=false
            try {
                    _categoryList.postValue(result)
                Log.e("success","response$result")
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
        fetchCategoryList()
    }

}