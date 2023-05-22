package com.example.moqayda.ui.swap_public_offers_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapPublicOffersDetailsBinding
import com.example.moqayda.repo.FirebaseRepo
import kotlin.properties.Delegates

class SwapPublicOffersDetailsFragment :
    BaseFragment<FragmentSwapPublicOffersDetailsBinding, SwapPublicOffersDetailsViewModel>(),
    Navigator {
    private var senderProductId by Delegates.notNull<Int>()
    private var receiverProductId by Delegates.notNull<Int>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomAppBar()
        viewDataBinding.vm = viewModel
        viewModel.navigator = this
        senderProductId =
            SwapPublicOffersDetailsFragmentArgs.fromBundle(requireArguments()).senderProductId
        receiverProductId =
            SwapPublicOffersDetailsFragmentArgs.fromBundle(requireArguments()).receiverProductId

        viewModel.getReceiverItemDetails(receiverProductId)
        viewModel.getSenderItemDetails(senderProductId)
        subscribeToLiveData()
        observeToLiveData()


    }

    private fun productAvailable() {
        viewDataBinding.linearLayout2.visibility = VISIBLE
        viewDataBinding.offerStatus.visibility = GONE
        viewDataBinding.deleteOffer.visibility = GONE
    }

    @SuppressLint("LongLogTag")
    fun observeToLiveData() {

        viewModel.senderProduct.observe(viewLifecycleOwner) { senderProduct ->
            viewDataBinding.senderProduct = senderProduct
            senderProduct.productAndOwnerViewModels?.forEach { productOwner ->
                if (productOwner != null) {
                    viewModel.productOwnerID.set(productOwner.id)
                }
            }
            viewModel.receiverProduct.observe(viewLifecycleOwner) { receiverProduct ->
                viewDataBinding.receiverProduct = receiverProduct

                if (senderProduct?.isActive == false) {
                    viewDataBinding.offerStatus.text =
                        getString(R.string.sender_product_not_available)
                    viewDataBinding.offerStatus.visibility = VISIBLE
                    viewDataBinding.deleteOffer.visibility = VISIBLE
                } else if (receiverProduct?.isActive == false) {
                    viewDataBinding.offerStatus.text =
                        getString(R.string.receiver_product_not_available)
                    viewDataBinding.offerStatus.visibility = VISIBLE
                    viewDataBinding.deleteOffer.visibility = VISIBLE
                } else {
                    productAvailable()
                }
            }
        }
        viewModel.senderUser.observe(viewLifecycleOwner) {
            viewDataBinding.senderUser = it
        }

        viewModel.progressBar.observe(viewLifecycleOwner){
            viewDataBinding.progressBar.isVisible = it
        }
    }


    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapPublicOffersDetailsViewModel {
        val vmFactory = SwapPublicOffersDetailsVMFactory(requireContext())
        return ViewModelProvider(this, vmFactory)[SwapPublicOffersDetailsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_public_offers_details
    }

    override fun onNavigateToProfileFragment() {
        findNavController().navigate(R.id.profileFragment)
    }

    override fun onNavigateToChatRequestsFragment() {
        this.findNavController().navigate(SwapPublicOffersDetailsFragmentDirections.actionSwapPublicOffersDetailsFragmentToRequestFragment())
    }

}