package com.example.moqayda.ui.product

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentListProductsBinding
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.CategoryProductViewModel

class ProductsListFragment: BaseFragment<FragmentListProductsBinding,ProductViewModel>() {

    private  var categoryId:Int = 0
    private lateinit var adapter : ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId=ProductsListFragmentArgs.fromBundle(requireArguments()).categoryId
        viewDataBinding.vm=viewModel

        getProductsById()
        observeToLiveData()
    }

    private fun getProductsById() {
        categoryId.let {id->
            Log.e("ProductsListFragment",id.toString())
            viewModel.getProductsById(id)
        }
    }

    private fun navigateToProductDetails(productItem: CategoryProductViewModel) {
            findNavController().
        navigate(ProductsListFragmentDirections.
        actionProductsListFragmentToProductDetailsFragment(
            productItem.name,productItem.productToSwap,productItem.descriptions))
    }

    private fun observeToLiveData() {
        viewModel.categoryItem.observe(viewLifecycleOwner) { item ->
            Log.e("ProductsListFragment", item.toString())
            initAdapter(item)
        }
        viewModel.isVisibleProgress.observe(viewLifecycleOwner) { isVisibleProgress ->
            viewDataBinding.progressBar.isVisible = isVisibleProgress
        }
    }

    private fun initAdapter(item: CategoryItem) {
        adapter = ProductAdapter(item.categoryProductViewModels)
        viewDataBinding.recyclerView.adapter = adapter
        adapter.onItemClickListener=object : ProductAdapter.OnItemClickListener{
            override fun onItemClick(productItem: CategoryProductViewModel?) {
                productItem?.let { navigateToProductDetails(it) }
            }
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