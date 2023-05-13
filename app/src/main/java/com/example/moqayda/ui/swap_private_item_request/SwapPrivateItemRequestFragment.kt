package com.example.moqayda.ui.swap_private_item_request

import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat.Token.fromBundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapRequestBinding

class SwapPrivateItemRequestFragment : BaseFragment<FragmentSwapRequestBinding,SwapPrivateItemRequestViewModel>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
        viewModel.privateItemId=SwapPrivateItemRequestFragmentArgs.fromBundle(requireArguments()).privateItemId
        viewModel.privateItemName=SwapPrivateItemRequestFragmentArgs.fromBundle(requireArguments()).privateItemName
        viewModel.privateItemImage=SwapPrivateItemRequestFragmentArgs.fromBundle(requireArguments()).privateItemImage
        viewModel.product = SwapPrivateItemRequestFragmentArgs.fromBundle(requireArguments()).product
        subscribeToLiveData()
    }
    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapPrivateItemRequestViewModel {
        return ViewModelProvider(this)[SwapPrivateItemRequestViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_request
    }

}