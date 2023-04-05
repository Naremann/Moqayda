package com.example.moqayda.ui.registeration
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentRegisterationBinding

class RegistrationFragment : BaseFragment<FragmentRegisterationBinding, RegisterViewModel>(),Navigator{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
        subscribeToLiveData()
        viewModel.navigator=this

    }



    override fun initViewModeL(): RegisterViewModel {
        return ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_registeration
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun navigateToLoginFragment() {
        requireView().findNavController().navigate(R.id.login)
    }
}