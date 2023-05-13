package com.example.moqayda.ui.swapping_items

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwappingItemBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.Product

class SwappingItemFragment : BaseFragment<FragmentSwappingItemBinding, SwappingItemViewModel>(),
    Navigator {
    private lateinit var selectedProduct: Product
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomAppBar()
        hideBottomAppBar()
        viewDataBinding.vm = viewModel
        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,
            getString(R.string.swap_now_text),
            this)
        selectedProduct = SwappingItemFragmentArgs.fromBundle(requireArguments()).selectedProduct
        viewModel.itemName = selectedProduct.name
        viewModel.itemId = selectedProduct.id
        viewModel.receiverId = selectedProduct.userId
        viewModel.getReceiverData()
        viewModel.navigator = this

        viewModel.appUser.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.e("SwappingItemFragment", it.firstName!!)
                viewDataBinding.appUser = it
            }
        }


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

    override fun navigateToPrivateProductFragment() {
        viewModel.isCheckedPrivateBtn.observe(viewLifecycleOwner) { isChecked ->
            findNavController().navigate(
                SwappingItemFragmentDirections.actionSwappingItemFragmentToPrivateProductsFragment(
                    isChecked,selectedProduct
                )
            )

        }
    }

    override fun navigateToUserPublicProductsFragment() {
        viewModel.isCheckedPublicBtn.observe(viewLifecycleOwner) { isChecked ->
            findNavController().navigate(
                SwappingItemFragmentDirections.actionSwappingItemFragmentToUserPublicItemsFragment(
                    isChecked,selectedProduct
                )
            )

        }
    }


}