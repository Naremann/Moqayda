package com.example.moqayda.ui.selectCategory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.Product
import com.example.moqayda.repo.category.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SelectCategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    BaseViewModel<Navigator>() {

    lateinit var navigator: Navigator
    var progressBarVisible = MutableLiveData<Boolean>()

    private val _categoryList = MutableLiveData<List<CategoryItem?>?>()
    val categoryList: MutableLiveData<List<CategoryItem?>?>
        get() = _categoryList


    private fun fetchCategoryList() {
        progressBarVisible.value = true

        viewModelScope.launch {
            val result = categoryRepository.getCategories()
            progressBarVisible.value = false
            try {
                _categoryList.postValue(result)
            } catch (ex: Exception) {
                messageLiveData.value = ex.localizedMessage

            }
        }
    }

    fun navigateToAddProduct(categoryItem: CategoryItem){
        navigator.onNavigateToAddProductFragment(categoryItem)
    }

    fun navigateToUpdateProduct(categoryId: Int,product: Product){
        navigator.onNavigateToUpdateProductFragment(categoryId,product)
    }

    init {

        fetchCategoryList()

    }

}