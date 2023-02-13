package com.example.moqayda.ui.password_verification

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentPasswordVerificationBinding

class PasswordVerificationFragment :
    BaseFragment<FragmentPasswordVerificationBinding, PasswordVerificationViewModel>() ,Navigator{
    lateinit var resendCodeBtn : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.navigator=this
        subscribeToLiveData()
        observeToLiveData()
    }

    private fun initViews() {
        resendCodeBtn = viewDataBinding.resendCodeBtn
    }

    private fun observeToLiveData() {
        viewModel.isActiveButton.observe(viewLifecycleOwner, Observer { isActiveBtn->
            resendCodeBtn.isEnabled = isActiveBtn
        })
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): PasswordVerificationViewModel {
        return ViewModelProvider(this)[PasswordVerificationViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_password_verification
    }

    override fun navigateToChangePassFragment() {
        requireView().findNavController().navigate(R.id.passwordChangeFragment)
    }

}