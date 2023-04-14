package com.example.moqayda.ui.products

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentProductsBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.CategoryProductViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductFragment : BaseFragment<FragmentProductsBinding, ProductViewModel>() {

    private var categoryId: Int = 0
    private var adapter: ProductAdapter = ProductAdapter()
    private var productList: MutableList<CategoryProductViewModel> = mutableListOf()
    private var filteredList: MutableList<CategoryProductViewModel> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility = View.VISIBLE
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
        activity?.findViewById<FloatingActionButton>(R.id.fabButton)?.hide()
        categoryId = ProductFragmentArgs.fromBundle(requireArguments()).categoryId
        viewDataBinding.vm = viewModel
        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,getString(R.string.Swap_items),this)
        getProductsById()
        observeToLiveData()
        initRecycler()
        searchItems()
    }

    private fun searchItems() {
        viewDataBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterItemsList(newText)
                return false
            }
        })
    }

    private fun filterItemsList(newText: String?) {
        filteredList.clear()
        for (item in productList) {
            for (i in 0 until item.name!!.length) {
                if (newText != null) {
                    if (newText.lowercase().contains(item.name[i].lowercase())
                    ) {
                        if (item !in filteredList)
                            item.let { filteredList.add(it) }
                    }
                }
            }
        }
        if (filteredList.isEmpty() && newText?.isNotBlank() == true) {
            showToastMessage("No Data Match..", Toast.LENGTH_SHORT)
        } else {
            adapter.changeData(filteredList)
            adapter.notifyDataSetChanged()
        }
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
            viewModel.categoryItem.observe(viewLifecycleOwner) { categoryItem ->
                Log.e("ProductsListFragment", categoryItem.toString())
                adapter.changeData(categoryItem?.categoryProductViewModels)
                adapter.notifyDataSetChanged()
                productList =
                    categoryItem?.categoryProductViewModels as MutableList<CategoryProductViewModel>
            }
            viewModel.isVisibleProgress.observe(viewLifecycleOwner) { isVisibleProgress ->
                viewDataBinding.progressBar.isVisible = isVisibleProgress
            }
            viewModel.connectionError.observe(viewLifecycleOwner) { error ->
                if (error != null) {
                    showToastMessage(error)
                }
            }
        }
    }

    private fun initRecycler() {
        viewDataBinding.recyclerView.adapter = adapter
        adapter.onItemClickListener = object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(productItem: CategoryProductViewModel?) {
                productItem?.let { navigateToProductDetails(productItem) }
            }
        }
        adapter.onActiveLoveImage = object : ProductAdapter.OnActiveLoveImageClickListener {
            override fun onIconClick(
                activeLoveImage: ImageView, inActiveLoveImage: ImageView,
                addToFavoriteTv: TextView, product: CategoryProductViewModel
            ) {
                product.id?.let {
                    deleteItemFromFavorite(
                        activeLoveImage,
                        inActiveLoveImage,
                        addToFavoriteTv,
                        product.id
                    )
                }
            }

        }
        adapter.onInActiveLoveImage = object : ProductAdapter.OnInActiveLoveImageClickListener {
            override fun onIconClick(
                activeLoveImage: ImageView,
                inActiveLoveImage: ImageView,
                addToFavoriteTv: TextView,
                product: CategoryProductViewModel
            ) {
                product.id?.let {
                    addItemToFavorite(
                        activeLoveImage, inActiveLoveImage, addToFavoriteTv, it
                    )
                }
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



