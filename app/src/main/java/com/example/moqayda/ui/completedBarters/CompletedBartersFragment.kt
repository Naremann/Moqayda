package com.example.moqayda.ui.completedBarters

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentCompletedBarterBinding
import com.example.moqayda.initToolbar

class CompletedBartersFragment :
    BaseFragment<FragmentCompletedBarterBinding, CompletedBartersViewModel>() {
    private lateinit var adapter: CompletedBartersAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomAppBar()
        viewDataBinding.toolbar.initToolbar(
            viewDataBinding.toolbar,
            requireActivity().getString(R.string.completed_barters),
            this
        )

        viewModel.barters.observe(viewLifecycleOwner) {
            val list = it?.reversed()

            if (list!!.isEmpty()){
                viewDataBinding.noBarters.visibility = VISIBLE
            }

            adapter = CompletedBartersAdapter(list, requireContext(), this, this)
            viewDataBinding.bartersRecycler.adapter = adapter
        }

        viewModel.progressBarStatus.observe(viewLifecycleOwner){
            viewDataBinding.progressBar.isVisible = it
        }
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): CompletedBartersViewModel {
        val vmFactory = CompletedBartersViewModelFactory(requireActivity())
        return ViewModelProvider(this, vmFactory)[CompletedBartersViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_completed_barter
    }
}