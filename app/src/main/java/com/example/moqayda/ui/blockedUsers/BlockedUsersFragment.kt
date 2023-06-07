package com.example.moqayda.ui.blockedUsers

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentBlockedUsersBinding
import com.example.moqayda.initToolbar

class BlockedUsersFragment : BaseFragment<FragmentBlockedUsersBinding, BlockedUsersViewModel>(),
    Navigator {
    private lateinit var adapter: BlockedUsersAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()

        viewDataBinding.toolbar.initToolbar(
            viewDataBinding.toolbar,
            getString(R.string.blocked_users),
            this
        )

        viewModel.blockedUsers.observe(viewLifecycleOwner) {
            val list = it.reversed()
            adapter = BlockedUsersAdapter(list, requireContext(), viewModel)
            viewDataBinding.blockedRecycler.adapter = adapter
        }

        viewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                viewDataBinding.noBlockedUsers.visibility = VISIBLE
            }
        }
        viewModel.progressBarStatus.observe(viewLifecycleOwner) {
            viewDataBinding.progressBar.isVisible = it
        }

    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): BlockedUsersViewModel {
        val vmFactory = BlockedUsersVMFactory(requireContext())
        return ViewModelProvider(this, vmFactory)[BlockedUsersViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_blocked_users
    }
}