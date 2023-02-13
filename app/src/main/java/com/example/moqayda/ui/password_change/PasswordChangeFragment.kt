package com.example.moqayda.ui.password_change

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentPasswordChangeBinding
import com.example.moqayda.ui.login.LoginViewModel

class PasswordChangeFragment : BaseFragment<FragmentPasswordChangeBinding,PasswordChangeViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
    }
    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): PasswordChangeViewModel {
        return ViewModelProvider(this)[PasswordChangeViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_password_change
    }
}