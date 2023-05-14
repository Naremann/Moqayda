package com.example.moqayda.ui.otherUserProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentOtherUserProfileBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.AppUser

class OtherUserProfileFragment:BaseFragment<FragmentOtherUserProfileBinding,OtherUserProfileViewModel>(),Navigator {
    private lateinit var selectedUser:AppUser
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomAppBar()

        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,getString(R.string.user_information),this)

        viewDataBinding.vm=viewModel
        viewModel.navigator=this
        selectedUser = OtherUserProfileFragmentArgs.fromBundle(requireArguments()).selectedUser
        viewDataBinding.appUser = selectedUser
    }


    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): OtherUserProfileViewModel {
        return ViewModelProvider(this)[OtherUserProfileViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_other_user_profile
    }

    override fun onNavigateToUserPublicProducts() {
        Log.e("UserProfileFragment","onNavigateToUserPublicProducts")
    }

    override fun onStartFullImageScreen() {
        selectedUser.image.let {
            if(it!=null){
                val intent = Intent(requireContext(), ImageViewerActivity::class.java)
                intent.putExtra("image_url", it)
                startActivity(intent)
            }
            else{
                showToastMessage("No Image")
            }
        }

    }
}