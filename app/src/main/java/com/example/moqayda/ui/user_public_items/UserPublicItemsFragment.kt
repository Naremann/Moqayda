package com.example.moqayda.ui.user_public_items

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentUserPublicProductsBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.PrivateItem
import com.example.moqayda.models.PrivateProduct
import com.example.moqayda.models.Product
import com.example.moqayda.models.ProductOwnerItem
import kotlinx.coroutines.launch


class UserPublicItemsFragment :
    BaseFragment<FragmentUserPublicProductsBinding, UserPublicItemViewModel>(), Navigator {
    private lateinit var adapter: UserPublicItemAdapter
    private var productList: MutableList<PrivateProduct> = mutableListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm = viewModel
        viewModel.navigator = this
        adapter = UserPublicItemAdapter(
            userPublicItemsFragment = this,
            mContext = this.requireContext(), owner = this
        )

        viewDataBinding.toolbar.initToolbar(
            viewDataBinding.toolbar,
            getString(R.string.items_in_public),
            this
        )

        subscribeToLiveData()
        observeToLiveData()
        initRecycler()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeToLiveData() {
        viewModel.product.observe(viewLifecycleOwner) { ProductsList ->

            if (ProductsList!!.isEmpty()){
                viewDataBinding.noProducts.visibility = VISIBLE
            }

            adapter.changeData(ProductsList)
            adapter.notifyDataSetChanged()

        }
        viewModel.isVisibleProgress.observe(viewLifecycleOwner) { isVisibleProgress ->
            viewDataBinding.progressBar.isVisible = isVisibleProgress
        }


    }

    private fun initRecycler() {

        viewDataBinding.recyclerView.adapter = adapter
        adapter.onSwapLinearClickListener =
            object : UserPublicItemAdapter.OnSwapLinearClickListener {
                override fun onSwapLinearClick(ProductItem: Product?) {
                    viewModel.senderProduct = ProductItem
                    addProductOwner(ProductItem?.id!!)
                    navigateToSwapPublicItemRequestFragment(ProductItem)
                }

            }

    }

    fun addProductOwner(ProductId: Int) {
        val userId = DataUtils.USER?.id
        showProgressDialog()

        val productOwnerItem = ProductOwnerItem(0, ProductId, userId!!)
        lifecycleScope.launch {
            val response = retrofitService.addProductOwner(productOwnerItem)
            hideProgressDialog()
            try {
                if (response.isSuccessful) {
                    Log.e("addProductOwner", "Success ${response.body()}")
                } else
                    Log.e("addPrivateItemOwner", "Fail :Unknown")

            } catch (ex: Exception) {
                Log.e("addProductOwner", "Fail ${ex.localizedMessage}")
            }
        }
    }


    private fun navigateToSwapPublicItemRequestFragment(senderRequestProduct: Product) {
        val product = UserPublicItemsFragmentArgs.fromBundle(requireArguments()).product
        findNavController().navigate(
            UserPublicItemsFragmentDirections
                .actionUserPublicItemsFragmentToSwapPublicItemRequestFragment(
                    senderRequestProduct,
                    product
                )
        )

    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): UserPublicItemViewModel {
        val viewModelFactory = UserPublicItemViewModelFactory(requireContext())
        return ViewModelProvider(this, viewModelFactory)[UserPublicItemViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_public_products
    }

    override fun onNavigateToProductDetails(product: Product) {
        findNavController().navigate(
            UserPublicItemsFragmentDirections.actionUserPublicItemsFragmentToProductDetailsFragment(
                product
            )
        )
    }

    override fun onNavigateTOUpdateProductFragment(product: Product, categoryId: Int) {
        findNavController().navigate(
            UserPublicItemsFragmentDirections.actionUserPublicItemsFragmentToUpdateProductFragment(
                product,
                categoryId
            )
        )
    }

}