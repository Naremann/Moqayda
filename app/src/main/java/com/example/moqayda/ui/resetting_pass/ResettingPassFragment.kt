package com.example.moqayda.ui.resetting_pass

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentResettingPassBinding



class ResettingPassFragment : BaseFragment<FragmentResettingPassBinding, ResettingPassViewModel>(),Navigator {

    lateinit var btn : Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       viewDataBinding.vm=viewModel
        btn = viewDataBinding.resetPassBtn
        viewModel.navigator=this
        subscribeToLiveData()

    }
    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): ResettingPassViewModel {
       return ViewModelProvider(this)[ResettingPassViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_resetting_pass
    }

    override fun navigateToPassVerificationFragment() {
        requireView().findNavController().navigate(R.id.passwordVerificationFragment)
    }

}