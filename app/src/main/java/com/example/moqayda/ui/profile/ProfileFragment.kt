package com.example.moqayda.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.database.downloadFirebaseStorageImage
import com.example.moqayda.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding,ProfileViewModel>() ,Navigator{
    var imageUri : Uri?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomAppBar()
        hideFloatingBtn()
        viewDataBinding.vm=viewModel
        viewModel.navigator=this

    }

    override fun onStart() {
        super.onStart()
        showUserImage()
    }
    private fun showUserImage() {
        DataUtils.USER?.id?.let {
            downloadFirebaseStorageImage({ uri->
                imageUri=uri
                Glide.with(viewDataBinding.userImage.context).load(uri).into(viewDataBinding.userImage)
            }, { ex->
                ex.localizedMessage?.let { it1 -> showToastMessage(it1) }
            }, it)
        }
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): ProfileViewModel {
        return ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }
    override fun navigateToLoginFragment() {

        showAlertDialog(getString(R.string.want_exit),getString(R.string.ok),
            { dialog, which ->
                findNavController().setGraph(R.navigation.nav_graph_authentication)
                findNavController().navigate(R.id.login)
        },getString(R.string.cancel))
    }

    override fun navigateToProfileEditing() {
        findNavController().navigate(R.id.profileEditingFragment)
    }

    override fun startFullImageScreen() {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        Log.e("imageFullScreen","uri : $imageUri")
        intent.setDataAndType(imageUri, "image/*")
        startActivity(intent)
    }

}