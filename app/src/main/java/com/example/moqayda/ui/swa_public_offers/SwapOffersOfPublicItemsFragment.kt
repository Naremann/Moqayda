package com.example.moqayda.ui.swa_public_offers

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapOffersOfPublicItemsBinding
import com.example.moqayda.initToolbar

class SwapOffersOfPublicItemsFragment : BaseFragment<FragmentSwapOffersOfPublicItemsBinding,SwapOffersOfPublicItemsViewModel>(),Navigator {
    var adapter = SwapPublicOffersAdapter(swapPublicOffersFragment = this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator=this
        observeToLiveData()
        initRecycler()

        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,getString(R.string.swap_offers),this)

    }
    private fun observeToLiveData() {
        viewModel.swappublicOffers.observe(viewLifecycleOwner){ swapPublicOffers->
            val list = swapPublicOffers?.reversed()
            adapter.changeData(list)
            adapter.notifyDataSetChanged()
        }

        viewModel.isVisibleProgressBar.observe(viewLifecycleOwner){isVisible->
            viewDataBinding.progressBar.isVisible=isVisible
        }

        viewModel.toastMessage.observe(viewLifecycleOwner){toastMessage->
            showToastMessage(toastMessage)

        }

        viewModel.isEmpty.observe(viewLifecycleOwner){isEmpty ->
            if (isEmpty){
                viewDataBinding.noOffers.visibility = View.VISIBLE
            }
        }


    }
    private fun initRecycler() {
        viewDataBinding.recyclerView.adapter = adapter
        adapter.onDetailsClickListener=object:SwapPublicOffersAdapter.OnDetailsClickListener{
            override fun onItemClick(senderProductId: Int, receiverProductId: Int) {
                navigateToSwapPublicOffersDetailsFragment(senderProductId,receiverProductId)
            }

        }


    }
    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapOffersOfPublicItemsViewModel {
        return ViewModelProvider(this)[SwapOffersOfPublicItemsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_offers_of_public_items
    }

     fun navigateToSwapPublicOffersDetailsFragment(senderProductId:Int,receiverProductId:Int)
     {
        findNavController().navigate(SwapOffersOfPublicItemsFragmentDirections.actionSwapOffersOfPublicItemsFragmentToSwapPublicOffersDetailsFragment(senderProductId,receiverProductId))
    }

}