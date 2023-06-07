package com.example.moqayda.ui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.HomeActivity
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.database.local.LanguagesSettingsHelper
import com.example.moqayda.database.local.LocaleHelper
import com.example.moqayda.database.local.ThemeModeSettingHelper.Companion.getThemeMode
import com.example.moqayda.database.local.ThemeModeSettingHelper.Companion.saveCurrentThemMode
import com.example.moqayda.databinding.FragmentSettingBinding
import com.example.moqayda.initToolbar

class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>(),AdapterView.OnItemSelectedListener,Navigator{
    private lateinit var adapter: ArrayAdapter<String>
    private var spinnerList: MutableList<String> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
        viewModel.navigator=this
        subscribeToLiveData()
        viewDataBinding.spinnerLanguage.onItemSelectedListener=this
        initSpinner()
        checkCurrentLanguage()
        hideBottomAppBar()
        hideFloatingBtn()
        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,getString(R.string.setting),this)
        viewDataBinding.switcher.setOnClickListener {
            checkSwitcherMode()
        }

    }
    override fun onStart() {
        super.onStart()
        Log.e("OnStart()","IsChecked ${viewDataBinding.switcher.isChecked}")
        viewDataBinding.switcher.isChecked= getThemeMode(requireContext())

    }

    private fun checkSwitcherMode() {
        if(viewDataBinding.switcher.isChecked){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else
        {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)}
        Log.e("SwitcherIsClicked","IsChecked ${viewDataBinding.switcher.isChecked}")
        saveCurrentThemMode(requireContext(),viewDataBinding.switcher.isChecked)
    }

    private fun checkCurrentLanguage() {
        if (LanguagesSettingsHelper.retreiveDataFromSharedPreferences("lang", requireContext()) == "ar") {
            setEnglishInSpinnerList(1)
        } else {
            setEnglishInSpinnerList(0)
        }
    }

    private fun convertAppLanguage(position:Int) {
        if (spinnerList[position] == getString(R.string.arabic_lan)) {
            saveCurrentLanguage("ar")
            viewDataBinding.spinnerLanguage.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?, position: Int, id: Long
                ) {
                    convertedAppLangToSelected(position,getString(R.string.english_lan),"en")
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        } else {
            saveCurrentLanguage("en")
            viewDataBinding.spinnerLanguage.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?, position: Int, id: Long
                ) {
                    convertedAppLangToSelected(position,getString(R.string.arabic_lan),"ar")
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun convertedAppLangToSelected(position:Int,spinnerItem: String,language: String) {
        if (spinnerList[position] == spinnerItem) {
            saveCurrentLanguage(language)
            startHomeActivity()
        }
    }

    private fun initSpinner() {
        getLanguageList()
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewDataBinding.spinnerLanguage.adapter = adapter

    }

    private fun getLanguageList() {
        spinnerList.add(0, getString(R.string.english_lan))
        spinnerList.add(1, getString(R.string.arabic_lan))
    }

    private fun saveCurrentLanguage(language: String) {
        LanguagesSettingsHelper.putData(language, requireContext())
        LocaleHelper.setLocale(requireContext(), language)
    }

    private fun setEnglishInSpinnerList(spinnerAddIndex: Int) {
        spinnerList.removeAt(0)
        spinnerList.add(spinnerAddIndex, getString(R.string.english_lan))
        adapter.notifyDataSetChanged()
        adapter.setNotifyOnChange(true)

    }

    private fun startHomeActivity() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        convertAppLanguage(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }




    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SettingViewModel {
        return ViewModelProvider(this)[SettingViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun navigateToRegisterFragment() {
        showAlertDialog(getString(R.string.want_delete_account),getString(R.string.ok),{ _, _ ->
            findNavController().setGraph(R.navigation.nav_graph_authentication)
            findNavController().navigate(R.id.registeration)
        },getString(R.string.cancel))
    }
    override fun navigateToAppTermsFragment() {
        findNavController().navigate(R.id.appTermsFragment)
    }

    override fun onNavigateToBlockedUsersFragment() {
        findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToBlockedUsersFragment())
    }


}