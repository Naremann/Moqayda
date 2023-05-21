package com.example.moqayda.ui.swap_public_offers_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapPublicOffersDetailsBinding
import kotlin.properties.Delegates

class SwapPublicOffersDetailsFragment :
    BaseFragment<FragmentSwapPublicOffersDetailsBinding, SwapPublicOffersDetailsViewModel>(),
    Navigator {
    private var senderProductId by Delegates.notNull<Int>()
    private var receiverProductId by Delegates.notNull<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm = viewModel
        viewModel.navigator = this
        senderProductId =
            SwapPublicOffersDetailsFragmentArgs.fromBundle(requireArguments()).senderProductId
        receiverProductId =
            SwapPublicOffersDetailsFragmentArgs.fromBundle(requireArguments()).receiverProductId

        viewModel.getReceiverItemDetails(receiverProductId)
        viewModel.getSenderItemDetails(senderProductId)

        observeToLiveData()



    }


    @SuppressLint("LongLogTag")
    fun observeToLiveData() {
        viewModel.senderProduct.observe(viewLifecycleOwner) {
            viewDataBinding.senderProduct = it
            it.productAndOwnerViewModels?.forEach { productOwner ->
                if (productOwner != null) {
                    viewModel.productOwnerID.set(productOwner.id)
                }
            }
        }
        viewModel.receiverProduct.observe(viewLifecycleOwner) {
            viewDataBinding.receiverProduct = it
        }

        viewModel.senderUser.observe(viewLifecycleOwner) {
            viewDataBinding.senderUser = it
        }


    }


    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapPublicOffersDetailsViewModel {
        val vmFactory = SwapPublicOffersDetailsVMFactory(requireContext())
        return ViewModelProvider(this,vmFactory)[SwapPublicOffersDetailsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_public_offers_details
    }

    override fun onNavigateToPublicSwapOffersFragment() {
        findNavController().navigate(R.id.swapOffersOfPublicItemsFragment)
    }

}