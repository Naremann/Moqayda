package com.example.moqayda.ui.registeration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentRegisterationBinding

class RegistrationFragment : BaseFragment<FragmentRegisterationBinding, RegisterViewModel>(),Navigator,
    AdapterView.OnItemSelectedListener {

    private var city : String?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
        subscribeToLiveData()
        viewModel.navigator=this
        viewDataBinding.spinner.onItemSelectedListener = this
        initSpinner()

    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(requireContext(),
            R.array.cities_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            viewDataBinding.spinner.adapter = adapter
        }
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        city=viewDataBinding.spinner.selectedItem.toString()
        viewModel.city= city as String
        Log.e("onItemSelected","city $city")
        Log.e("onItemSelected","cityViewModel ${viewModel.city}")

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}