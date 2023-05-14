package com.example.moqayda.ui.product_details

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentProductDetailsBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.Product

class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding,ProductDetailsViewModel>(),Navigator {
    lateinit var selectedProduct: Product

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomAppBar()
        hideFloatingBtn()
        viewDataBinding.vm=viewModel
        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,getString(R.string.item_details),this)
        getProductDetails()
        viewModel.navigator=this



        viewModel.appUser.observe(viewLifecycleOwner){
            it.let {
                viewDataBinding.appUser = it
            }
        }

        viewDataBinding.productImage.setOnClickListener {
            val intent = Intent(requireContext(), ImageViewerActivity::class.java)
            intent.putExtra("image_url", selectedProduct.pathImage)
            startActivity(intent)
        }

    }



    private fun getProductDetails() {
        selectedProduct=ProductDetailsFragmentArgs.fromBundle(requireArguments()).selectedProduct
        viewModel.getProductOwner(selectedProduct.userId!!)
        viewDataBinding.product = selectedProduct
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): ProductDetailsViewModel {
        return ViewModelProvider(this)[ProductDetailsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_product_details
    }

    override fun navigateToSwappingItemsFragment() {
        findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToSwappingItemFragment(selectedProduct))
    }

    override fun onNavigateToUserProfile(appUser: AppUser) {
        findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToOtherUserProfileFragment(appUser))
    }
}