package com.example.moqayda.ui.otherUserProfile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentOtherUserProfileBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.Product
import com.example.moqayda.ui.user_public_items.UserPublicItemAdapter

class OtherUserProfileFragment:BaseFragment<FragmentOtherUserProfileBinding,OtherUserProfileViewModel>(),Navigator {
    private lateinit var selectedUser:AppUser
    private lateinit var adapter:UserPublicItemAdapter
    private lateinit var  builder: AlertDialog.Builder
    private lateinit var  dialog: AlertDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomAppBar()
        subscribeToLiveData()

        initBlockDialog()
        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,getString(R.string.user_information),this)

        viewDataBinding.vm=viewModel
        viewModel.navigator=this
        selectedUser = OtherUserProfileFragmentArgs.fromBundle(requireArguments()).selectedUser
        viewDataBinding.appUser = selectedUser





        selectedUser.userProductViewModels.let {
            adapter = UserPublicItemAdapter(productList = selectedUser.userProductViewModels!!,
                userPublicItemsFragment = null,
                mContext = this.requireContext(),
                this)

            viewDataBinding.userProductsRecyclerview.adapter = adapter
        }


        viewDataBinding.blockUser.setOnClickListener {
            val animation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.click_animation)
            viewDataBinding.blockUser.startAnimation(animation)
            dialog.show()

        }


    }

    private fun initBlockDialog(){
        builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.confirmation))
        builder.setMessage(R.string.block_confirmation)

        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            viewModel.blockUser(selectedUser.id!!)
        }

        builder.setNegativeButton(getString(R.string.cancel)) { _, _ ->
            // Cancel button clicked

        }
        dialog = builder.create()
    }


    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): OtherUserProfileViewModel {
        val vmFactory = OtherUserProfileVMFactory(requireContext())
        return ViewModelProvider(this, vmFactory)[OtherUserProfileViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_other_user_profile
    }



    override fun onStartFullImageScreen() {
        selectedUser.image.let {
            if (it != null) {
                val intent = Intent(requireContext(), ImageViewerActivity::class.java)
                intent.putExtra("image_url", it)
                startActivity(intent)
            } else {
                showToastMessage("No Image")
            }
        }

    }

    override fun onNavigateToProductDetails(product: Product) {
        this.findNavController()
            .navigate(OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToProductDetailsFragment(
                product))
    }

    override fun onNavigateToHomeFragment() {
        this.findNavController().navigate(R.id.homeFragment)
    }
}