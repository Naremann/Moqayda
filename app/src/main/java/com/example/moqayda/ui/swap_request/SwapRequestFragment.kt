package com.example.moqayda.ui.swap_request

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapRequestBinding

class SwapRequestFragment : BaseFragment<FragmentSwapRequestBinding,SwapRequestViewModel>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
        viewModel.privateItemId=SwapRequestFragmentArgs.fromBundle(requireArguments()).privateItemId
        viewModel.privateItemName=SwapRequestFragmentArgs.fromBundle(requireArguments()).privateItemName
        viewModel.privateItemImage=SwapRequestFragmentArgs.fromBundle(requireArguments()).privateItemImage
        viewModel.product = SwapRequestFragmentArgs.fromBundle(requireArguments()).product
        subscribeToLiveData()
    }
    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapRequestViewModel {
        return ViewModelProvider(this)[SwapRequestViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_request
    }

}