package com.example.moqayda.ui.products

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentProductsBinding
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.CategoryProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductFragment : BaseFragment<FragmentProductsBinding, ProductViewModel>() {

    private var categoryId: Int = 0
    private lateinit var adapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId = ProductFragmentArgs.fromBundle(requireArguments()).categoryId
        viewDataBinding.vm = viewModel
        getProductsById()
        observeToLiveData()
    }

    private fun getProductsById() {
        categoryId.let { id ->
            Log.e("ProductsListFragment", id.toString())
            viewModel.getProductsById(id)
        }
    }

    private fun navigateToProductDetails(productItem: CategoryProductViewModel?) {
        findNavController().navigate(
            ProductFragmentDirections.actionProductsListFragmentToProductDetailsFragment(
                productItem?.name, productItem?.productToSwap, productItem?.descriptions
            )
        )
    }

    private fun observeToLiveData() {
        lifecycleScope.launch {
            viewModel.categoryItem.observe(viewLifecycleOwner) { item ->
                Log.e("ProductsListFragment", item.toString())
                item?.let { initAdapter(item) }
            }
            viewModel.isVisibleProgress.observe(viewLifecycleOwner) { isVisibleProgress ->
                viewDataBinding.progressBar.isVisible = isVisibleProgress
            }
            viewModel.connectionError.observe(viewLifecycleOwner) { error ->
                showToastMessage(error)
            }
        }
    }

    private fun initAdapter(item: CategoryItem?) {
        adapter = ProductAdapter(item?.categoryProductViewModels)
        viewDataBinding.recyclerView.adapter = adapter
        adapter.onItemClickListener = object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(productItem: CategoryProductViewModel?) {
                productItem?.let { navigateToProductDetails(productItem) }
            }
        }
        adapter.onActiveLoveImage = object : ProductAdapter.OnActiveLoveImageClickListener {
            override fun onIconClick(
                activeLoveImage: ImageView, inActiveLoveImage: ImageView, addToFavoriteTv: TextView
            ) {
                deleteItemFromFavorite(
                    activeLoveImage,
                    inActiveLoveImage,
                    addToFavoriteTv,
                    item?.id!!
                )
            }

        }
        adapter.onInActiveLoveImage = object : ProductAdapter.OnInActiveLoveImageClickListener {
            override fun onIconClick(
                activeLoveImage: ImageView,
                inActiveLoveImage: ImageView,
                addToFavoriteTv: TextView,
            ) {
                addItemToFavorite(
                    activeLoveImage, inActiveLoveImage, addToFavoriteTv, item?.id!!
                )
            }
        }
        adapter.onActiveLoveImage = object : ProductAdapter.OnActiveLoveImageClickListener {
            override fun onIconClick(
                activeLoveImage: ImageView,
                inActiveLoveImage: ImageView,
                addToFavoriteTv: TextView
            ) {
                activeLoveImage.isVisible = false
                inActiveLoveImage.isVisible = true
                addToFavoriteTv.isVisible = true
            }
        }
    }

    private fun deleteItemFromFavorite(
        activeLoveImage: ImageView, inActiveLoveImage: ImageView,
        addToFavoriteTv: TextView, productId: Int
    ) {
        activeLoveImage.isVisible = false
        inActiveLoveImage.isVisible = true
        addToFavoriteTv.isVisible = true
        //delete item from database
    }

    private fun addItemToFavorite(
        activeLoveImage: ImageView, inActiveLoveImage: ImageView,
        addToFavoriteTv: TextView, productId: Int
    ) {
        activeLoveImage.isVisible = true
        inActiveLoveImage.isVisible = false
        addToFavoriteTv.isVisible = false
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("on image click", "isActive$productId")
            //viewModel.addItemToFavorite(productId,"")

        }
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): ProductViewModel {
        return ViewModelProvider(this)[ProductViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_products
    }

}