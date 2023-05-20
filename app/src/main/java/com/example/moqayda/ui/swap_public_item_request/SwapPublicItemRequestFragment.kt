package com.example.moqayda.ui.swap_public_item_request

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapPublicItemRequestBinding
import kotlinx.coroutines.launch

class SwapPublicItemRequestFragment : BaseFragment<FragmentSwapPublicItemRequestBinding,SwapPublicItemViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
        getProductOwnerByProductId()
        viewDataBinding.vm=viewModel
        getSwapPublicItemRequestArgs()

    }
    private fun getProductOwnerByProductId(){
        val productId = SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).product?.id
        lifecycleScope.launch {
            val response= RetrofitBuilder.retrofitService.getProductById(productId).body()?.productAndOwnerViewModels

            try {
                response?.forEach { productAndOwnerViewModels->
                    viewModel.productOwnerId=productAndOwnerViewModels?.id
                    Log.e("getProductOwner","Success ${productAndOwnerViewModels?.id}")

                }



            }
            catch (ex:Exception){
                Log.e("getProductOwner","Fail ${ex.localizedMessage}")
            }
        }
    }

    private fun getSwapPublicItemRequestArgs() {
        viewModel.product=SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).product
        viewModel.requestSenderProduct=SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).senderRequestProduct
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapPublicItemViewModel {
        return ViewModelProvider(this)[SwapPublicItemViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_public_item_request
    }

}