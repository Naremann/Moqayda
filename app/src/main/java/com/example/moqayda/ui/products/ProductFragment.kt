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
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentProductsBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductFragment : BaseFragment<FragmentProductsBinding, ProductViewModel>(), Navigator {

    private var categoryId: Int = 0
    private lateinit var adapter: ProductAdapter
    private var productList: MutableList<Product> = mutableListOf()
    private var filteredList: MutableList<Product> = mutableListOf()
    private val filteredProductsML = mutableListOf<Product>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomAppBar()
        showFloatingBtn()
        categoryId = ProductFragmentArgs.fromBundle(requireArguments()).categoryId
        viewDataBinding.vm = viewModel
        viewDataBinding.toolbar.initToolbar(
            viewDataBinding.toolbar,
            getString(R.string.Swap_items),
            this
        )
        adapter = ProductAdapter(productList, viewModel)
        viewModel.navigator = this
        getProductsById()
        observeToLiveData()
        initRecycler()
        searchItems()
        subscribeToLiveData()
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
            showToastMessage(getString(R.string.no_match), Toast.LENGTH_SHORT)
            adapter.changeData(productList)
            adapter.notifyDataSetChanged()
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

//    private fun navigateToProductDetails(productItem: Product?) {
//        findNavController().navigate(
//            ProductFragmentDirections.actionProductsListFragmentToProductDetailsFragment(
//                productItem
//            )
//        )
//    }

    private fun observeToLiveData() {
        Log.e("BlockedUsersViewModel", DataUtils.USER?.id.toString())
        lifecycleScope.launch {
            viewModel.categoryItem.observe(viewLifecycleOwner) { categoryItem ->
                viewModel.userBlockageList.observe(viewLifecycleOwner) { userBlockageList ->
                    val filteredProductsML = mutableListOf<Product>()
                    categoryItem?.categoryProductViewModels?.forEach { product ->
                        val isBlocked = userBlockageList.any { userBlockage ->
                            (product?.userId == userBlockage.blockingUserId && DataUtils.USER?.id == userBlockage.blockedUserId) ||
                                    (product?.userId == userBlockage.blockedUserId && DataUtils.USER?.id == userBlockage.blockingUserId)
                        }
                        if (!isBlocked) {
                            product?.let {
                                filteredProductsML.add(it)
                            }
                        }
                    }
                    Log.e("ProductsListFragment", categoryItem.toString())
                    adapter.changeData(filteredProductsML)
                    adapter.notifyDataSetChanged()
                    productList = filteredProductsML
                }
            }
            viewModel.isVisibleProgress.observe(viewLifecycleOwner) { isVisibleProgress ->
                viewDataBinding.progressBar.isVisible = isVisibleProgress
            }

        }
    }

    private fun initRecycler() {
        viewDataBinding.recyclerView.adapter = adapter
        adapter.onItemClickListener = object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(productItem: Product?) {
                findNavController().navigate(
                    ProductFragmentDirections.actionProductsListFragmentToProductDetailsFragment(
                        productItem!!
                    )
                )
            }
        }

    }


    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): ProductViewModel {
        val vmFactory = ProductsViewModelFactory(requireContext())
        return ViewModelProvider(this, vmFactory)[ProductViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_products
    }

    override fun onNavigateToOwnerProfile(user: AppUser) {
        this.findNavController()
            .navigate(
                ProductFragmentDirections.actionProductsListFragmentToOtherUserProfileFragment(
                    user
                )
            )
    }

}



