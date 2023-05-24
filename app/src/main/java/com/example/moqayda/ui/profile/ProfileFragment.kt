package com.example.moqayda.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentProfileBinding
import com.example.moqayda.models.AppUser

class ProfileFragment : BaseFragment<FragmentProfileBinding,ProfileViewModel>() ,Navigator{
    var imageUrl : String?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomAppBar()
        hideFloatingBtn()
//        loadUserImage()
        viewDataBinding.vm=viewModel
        viewModel.navigator=this

        viewModel.appUser.observe(viewLifecycleOwner){
            viewDataBinding.appUser = it
            imageUrl = it.image
        }


    }

//    private fun loadUserImage(){
//        viewDataBinding.progressBar.isVisible=true
//        DataUtils.USER?.id?.let { userId ->
//            getUerImageFromFirebase(object: ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    DataUtils.USER!!.id?.let {
//                        getFirebaseImageUri({ uri->
//                            imageUri=uri
//                            viewDataBinding.progressBar.isVisible=false
//                            Picasso.with(viewDataBinding.userImage.context).load(uri).into(viewDataBinding.userImage)
//                        }, { ex->
//                            viewDataBinding.progressBar.isVisible=false
//                            ex.localizedMessage?.let { error -> showToastMessage(error) }
//
//                        }, userId)
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    showToastMessage("Error Loading Image")
//                }
//            }, userId)
//        }
//    }
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
                if(viewModel.isLoggedOut()){
                    hideBottomAppBar()
                    findNavController().setGraph(R.navigation.nav_graph_authentication)
                    findNavController().navigate(R.id.login)
                }
            },getString(R.string.cancel))
    }

    override fun navigateToProfileEditing(currentUser: AppUser) {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToProfileEdittingFragment(currentUser))
    }

    override fun startFullImageScreen() {
        if(imageUrl !=null){
            val intent = Intent(requireContext(), ImageViewerActivity::class.java)
            intent.putExtra("image_url", imageUrl)
            startActivity(intent)
        }
        else{
            showToastMessage("No Image")
        }
    }

    override fun navigateToSettingFragment() {
        findNavController().navigate(R.id.settingFragment)
    }

    override fun navigateToPrivateProducts() {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPrivateProductsFragment(false,null))
    }

    override fun navigateToUserPublicProducts() {

        findNavController().navigate(ProfileFragmentDirections
            .actionProfileFragmentToUserPublicItemsFragment(false,null))
    }

    override fun navigateToSwapPrivateOffersFragment() {
        findNavController().navigate(R.id.swapPrivateOffersFragment)
    }

    override fun navigateToSwapPublicOffersFragment() {
        findNavController().navigate(R.id.swapOffersOfPublicItemsFragment)
    }

    override fun onNavigateToCompletedBarterFragment() {
        this.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToCompletedBartersFragment())
    }

    override fun onNavigateToSentOffersFragment() {
        this.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSentOffersFragment())
    }


}