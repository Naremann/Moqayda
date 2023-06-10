package com.example.moqayda.ui.sentOffers

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSentOffersBinding
import com.example.moqayda.initToolbar

class SentOffersFragment:BaseFragment<FragmentSentOffersBinding,SentOffersViewModel>() {

    private lateinit var adapter:SentOffersAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomAppBar()
        viewDataBinding.toolbar.initToolbar(
            viewDataBinding.toolbar,
            requireActivity().getString(R.string.sent_offers),
            this
        )
        subscribeToLiveData()
        viewModel.sentOffers.observe(viewLifecycleOwner) {
            val list = it?.reversed()

            adapter = SentOffersAdapter(list!!, requireContext(), this, this)
            viewDataBinding.offersRecycler.adapter = adapter
        }

        viewModel.progressBarStatus.observe(viewLifecycleOwner){
            viewDataBinding.progressBar.isVisible = it
        }

        viewModel.isEmpty.observe(viewLifecycleOwner){isEmpty ->
            if (isEmpty){
                viewDataBinding.noSentOffers.visibility = VISIBLE
            }
        }
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SentOffersViewModel {
        val vmFactory = SentOffersVmFactory(requireContext())
        return ViewModelProvider(this,vmFactory)[SentOffersViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_sent_offers
    }
}