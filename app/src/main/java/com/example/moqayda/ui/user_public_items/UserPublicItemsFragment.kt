package com.example.moqayda.ui.user_public_items

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentUserPublicProductsBinding
import com.example.moqayda.models.PrivateItem
import com.example.moqayda.models.PrivateProduct
import com.example.moqayda.models.Product


class UserPublicItemsFragment : BaseFragment<FragmentUserPublicProductsBinding,UserPublicItemViewModel>(),Navigator{
    private lateinit var adapter: UserPublicItemAdapter
    private var productList: MutableList<PrivateProduct> = mutableListOf()
    private var filteredList: MutableList<PrivateItem> = mutableListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel

        adapter= UserPublicItemAdapter(userPublicItemsFragment = this,
            mContext = this.requireContext(), owner = this)

        subscribeToLiveData()
        observeToLiveData()
        initRecycler()
    }
    private fun observeToLiveData() {
        viewModel.product.observe(viewLifecycleOwner){ ProductsList->
            adapter.changeData(ProductsList)
            adapter.notifyDataSetChanged()

        }
        viewModel.isVisibleProgress.observe(viewLifecycleOwner) { isVisibleProgress ->
            viewDataBinding.progressBar.isVisible = isVisibleProgress
        }

    }
    private fun initRecycler() {

        viewDataBinding.recyclerView.adapter = adapter
        adapter.onSwapLinearClickListener=object:UserPublicItemAdapter.OnSwapLinearClickListener{
            override fun onSwapLinearClick(ProductItem: Product?) {
                viewModel.privateProduct=ProductItem
                viewModel.addProductOwner(ProductItem?.id!!)
//                navigateToSwapPublicItemRequestFragment(ProductItem)
            }

        }

    }

    private fun navigateToSwapPublicItemRequestFragment(senderRequestProduct: PrivateProduct) {
        val product = UserPublicItemsFragmentArgs.fromBundle(requireArguments()).product
        val productOwnerId = viewModel.productOwnerItemId.observe(viewLifecycleOwner){productOwnerId->
            findNavController().navigate(UserPublicItemsFragmentDirections
                .actionUserPublicItemsFragmentToSwapPublicItemRequestFragment(senderRequestProduct,product,productOwnerId))
        }

    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): UserPublicItemViewModel {
        return ViewModelProvider(this)[UserPublicItemViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_public_products
    }

    override fun onNavigateToProductDetails(product: Product) {
        findNavController().navigate(UserPublicItemsFragmentDirections.actionUserPublicItemsFragmentToProductDetailsFragment(product))
    }

}