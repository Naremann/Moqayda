package com.example.moqayda.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding,ProfileViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
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

}