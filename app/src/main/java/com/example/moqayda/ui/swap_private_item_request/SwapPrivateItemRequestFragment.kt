package com.example.moqayda.ui.swap_private_item_request

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapPrivateItemRequestBinding
import kotlinx.coroutines.launch

class SwapPrivateItemRequestFragment : BaseFragment<FragmentSwapPrivateItemRequestBinding,SwapPrivateItemRequestViewModel>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProductOwnerByProductId(SwapPrivateItemRequestFragmentArgs.fromBundle(requireArguments()).privateItemId)
        viewDataBinding.vm=viewModel
        getSwapPrivateItemRequestArgs()
        subscribeToLiveData()

    }

    private fun getSwapPrivateItemRequestArgs() {
        viewModel.privateItemId=SwapPrivateItemRequestFragmentArgs.fromBundle(requireArguments()).privateItemId
        viewModel.privateItemName=SwapPrivateItemRequestFragmentArgs.fromBundle(requireArguments()).privateItemName
        viewModel.privateItemImage=SwapPrivateItemRequestFragmentArgs.fromBundle(requireArguments()).privateItemImage
        viewModel.product = SwapPrivateItemRequestFragmentArgs.fromBundle(requireArguments()).product
    }

    private fun getProductOwnerByProductId(privateItemId:Int){
        showProgressDialog()
        lifecycleScope.launch {
           val response= retrofitService.getPrivateProductByItemId(privateItemId).privateItemAndOwnerViewModels
            hideProgressDialog()

            try {
                response?.forEach { privateItemAndOwnerViewModels->
                    viewModel.productOwnerId=privateItemAndOwnerViewModels?.id!!

                    Log.e("getProductOwner","Success ${privateItemAndOwnerViewModels.id}")

                }

            }
            catch (ex:Exception){
                Log.e("getProductOwner","Fail ${ex.localizedMessage}")
            }
        }
    }
    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapPrivateItemRequestViewModel {
        return ViewModelProvider(this)[SwapPrivateItemRequestViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_private_item_request
    }

}