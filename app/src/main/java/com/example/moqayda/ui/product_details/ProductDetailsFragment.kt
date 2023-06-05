package com.example.moqayda.ui.product_details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentProductDetailsBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProductDetailsFragment :
    BaseFragment<FragmentProductDetailsBinding, ProductDetailsViewModel>(), Navigator {
    lateinit var selectedProduct: Product

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomAppBar()
        hideFloatingBtn()
        viewDataBinding.vm = viewModel
        viewDataBinding.toolbar.initToolbar(
            viewDataBinding.toolbar,
            getString(R.string.item_details),
            this
        )
        viewModel.navigator = this

        getProductDetails()






        viewDataBinding.productImage.setOnClickListener {
            val intent = Intent(requireContext(), ImageViewerActivity::class.java)
            intent.putExtra("image_url", selectedProduct.pathImage)
            startActivity(intent)
        }

    }


    private fun getProductDetails() {
        selectedProduct = ProductDetailsFragmentArgs.fromBundle(requireArguments()).selectedProduct

        viewModel.getProductOwner(selectedProduct.userId!!)
        viewDataBinding.product = selectedProduct

        viewModel.appUser.observe(viewLifecycleOwner) {
            it.let {
                viewDataBinding.appUser = it
            }
            if (it.id != Firebase.auth.currentUser?.uid) {
                viewDataBinding.view.visibility = VISIBLE
                viewDataBinding.userImg.visibility = VISIBLE
                viewDataBinding.userName.visibility = VISIBLE
                viewDataBinding.userCity.visibility = VISIBLE
            }
            if (selectedProduct.isActive != false && it.id != Firebase.auth.currentUser?.uid) {
                viewDataBinding.button2.visibility = VISIBLE
                viewDataBinding.productStatus.visibility = GONE

            }
        }

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
        findNavController().navigate(
            ProductDetailsFragmentDirections.actionProductDetailsFragmentToSwappingItemFragment(
                selectedProduct
            )
        )
    }

    override fun onNavigateToUserProfile(appUser: AppUser) {
        findNavController().navigate(
            ProductDetailsFragmentDirections.actionProductDetailsFragmentToOtherUserProfileFragment(
                appUser
            )
        )
    }
}