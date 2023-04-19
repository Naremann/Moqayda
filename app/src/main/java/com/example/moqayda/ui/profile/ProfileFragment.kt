package com.example.moqayda.ui.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentProfileBinding
import com.example.moqayda.initToolbar

class ProfileFragment : BaseFragment<FragmentProfileBinding,ProfileViewModel>() ,Navigator{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomAppBar()
        hideFloatingBtn()
        viewDataBinding.vm=viewModel
        viewModel.navigator=this

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

}