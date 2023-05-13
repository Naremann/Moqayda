package com.example.moqayda.ui.swap_public_item_request

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapPublicItemRequestBinding

class SwapPublicItemRequestFragment : BaseFragment<FragmentSwapPublicItemRequestBinding,SwapPublicItemViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
        viewDataBinding.vm=viewModel
        getSwapPublicItemRequestArgs()

    }

    private fun getSwapPublicItemRequestArgs() {
        viewModel.product=SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).product
        viewModel.requestSenderProduct=SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).senderRequestProduct
        viewModel.productOwnerId=SwapPublicItemRequestFragmentArgs.fromBundle(requireArguments()).productOwnerId
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