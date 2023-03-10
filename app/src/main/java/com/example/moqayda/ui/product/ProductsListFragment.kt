package com.example.moqayda.ui.product

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentListProductsBinding
import com.example.moqayda.models.CategoryItem

class ProductsListFragment: BaseFragment<FragmentListProductsBinding,ProductViewModel>() {

    private lateinit var category:CategoryItem
    lateinit var adapter : ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        category = ProductsListFragmentArgs.fromBundle(requireArguments()).selectedCategory
        viewDataBinding.vm=viewModel
//        viewModel.categoryId= category.id!!

        category.id?.let {
            Log.e("ProductsListFragment",it.toString())
            viewModel.getProductsById(it)
        }

        observeToLiveData()
    }

    private fun observeToLiveData() {
//        viewModel.productList.observe(viewLifecycleOwner) { data ->
//            adapter = data?.let {productList->
//                ProductAdapter(productList)
//            }!!
//            viewDataBinding.recyclerView.adapter = adapter
//        }
//
//        viewModel.isVisibleProgress.observe(viewLifecycleOwner) { isVisibleProgress ->
//            viewDataBinding.progressBar.isVisible = isVisibleProgress
//        }
        viewModel.categoryItem.observe(viewLifecycleOwner) { item ->
            Log.e("ProductsListFragment", item.toString())
            adapter = ProductAdapter(item.categoryProductViewModels)
            viewDataBinding.recyclerView.adapter = adapter
        }
        viewModel.isVisibleProgress.observe(viewLifecycleOwner) { isVisibleProgress ->
            viewDataBinding.progressBar.isVisible = isVisibleProgress
        }
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): ProductViewModel {
        return ViewModelProvider(this)[ProductViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_list_products
    }

}