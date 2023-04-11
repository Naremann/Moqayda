package com.example.moqayda.ui.swapping_items

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwappingItemBinding
import com.example.moqayda.initToolbar

class SwappingItemFragment : BaseFragment<FragmentSwappingItemBinding, SwappingItemViewModel>() ,Navigator{
    private var itemName:String?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,getString(R.string.swap_now_text),this)
        itemName=SwappingItemFragmentArgs.fromBundle(requireArguments()).itemName
        viewModel.itemName=itemName


        
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwappingItemViewModel {
        return ViewModelProvider(this)[SwappingItemViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swapping_item
    }

    override fun navigateToAddPrivateProductFragment() {
        findNavController().navigate(R.id.addPrivateProductFragment)
    }


}