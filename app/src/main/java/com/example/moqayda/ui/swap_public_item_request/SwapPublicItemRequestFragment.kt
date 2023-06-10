package com.example.moqayda.ui.swap_public_item_request

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapPublicItemRequestBinding
import com.example.moqayda.initToolbar
import kotlinx.coroutines.launch

class SwapPublicItemRequestFragment :
    BaseFragment<FragmentSwapPublicItemRequestBinding, SwapPublicItemViewModel>(), Navigator {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
        getProductOwnerByProductId()
        viewModel.navigator = this
        viewDataBinding.vm = viewModel
        getSwapPublicItemRequestArgs()

        viewDataBinding.toolbar.initToolbar(
            viewDataBinding.toolbar,
            getString(R.string.request_swap),
            this
        )

    }

    private fun getProductOwnerByProductId() {
        showProgressDialog()
        val productId =
            SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).senderRequestProduct?.id
        lifecycleScope.launch {
            val response = RetrofitBuilder.retrofitService.getProductById(productId)
                .body()?.productAndOwnerViewModels
            hideProgressDialog()
            try {
                response?.forEach { productAndOwnerViewModels ->
                    viewModel.productOwnerId = productAndOwnerViewModels?.id
                    Log.e("getProductOwner", "Success ${productAndOwnerViewModels?.id}")

                }

            } catch (ex: Exception) {
                Log.e("getProductOwner", "Fail ${ex.localizedMessage}")
            }
        }
    }

    private fun getSwapPublicItemRequestArgs() {
        viewModel.receiverProduct.set(SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).product)
        viewDataBinding.receiverProduct =
            SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).product
        viewModel.senderProduct.set(SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).senderRequestProduct)
        viewDataBinding.senderProduct =
            SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).senderRequestProduct

        viewModel.getReceiver()
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapPublicItemViewModel {
        val vmFactory = SWPItemReqVMFactory(requireActivity())
        return ViewModelProvider(this, vmFactory)[SwapPublicItemViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_public_item_request
    }

    override fun onNavigateToHomeFragment() {
        findNavController().navigate(R.id.homeFragment)
    }

}