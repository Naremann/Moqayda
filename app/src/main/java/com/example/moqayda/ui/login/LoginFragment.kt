package com.example.moqayda.ui.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(), Navigator {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm = viewModel
        viewModel.navigator = this
        subscribeToLiveData()
    }

    override fun initViewModeL(): LoginViewModel {
        return ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun navigateToHomeActivity() {

    }


    override fun navigateToRegisterFragment() {
        requireView().findNavController().navigate(R.id.registeration)
    }

    override fun navigateToResettingPassFragment() {
        requireView().findNavController().navigate(R.id.resettingPassFragment)
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }
}