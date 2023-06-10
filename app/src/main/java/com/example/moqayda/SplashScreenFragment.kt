package com.example.moqayda

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.moqayda.database.getUserFromFirestore
import com.example.moqayda.database.local.LanguagesSettingsHelper
import com.example.moqayda.database.local.LocaleHelper
import com.example.moqayda.database.local.ThemeModeSettingHelper
import com.example.moqayda.databinding.FragmentSplashScreenBinding
import com.example.moqayda.models.AppUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenFragment() : androidx.fragment.app.Fragment() {
    private var binding : FragmentSplashScreenBinding?=null

    @Inject
    lateinit var networkHandler:NetworkHandler



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({ checkLoggedInUser(view) },3000)

    }
    private fun checkAppThemeMode() {
        if(ThemeModeSettingHelper.getThemeMode(requireContext()))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
    private fun setLocalLanguage() {
        val data = LanguagesSettingsHelper.retreiveDataFromSharedPreferences("lang", requireContext())
        if (data == "ar") {
            LocaleHelper.setLocale(requireContext(), "ar")
        } else {
            Log.e("lang", data)
            LocaleHelper.setLocale(requireContext(), "en")

        }
    }
    private fun showAlertDialog(
        message:String, posActionName:String?=null,
        posActionListener: DialogInterface.OnClickListener?=null,
        cancellable: Boolean = true
    ){
        val builder = AlertDialog.Builder(requireContext())
            .setMessage(message)
        if(posActionName != null){
            builder.setPositiveButton(posActionName, posActionListener)
        }

        builder.setCancelable(cancellable)
        builder.show()
    }
    private fun checkNetworkStatus() {
        if (!networkHandler.isOnline()) {
            showAlertDialog(requireContext().getString(R.string.no_internet), requireContext().getString(R.string.ok))
            NavHostFragment.findNavController(this).popBackStack()
        }
        if(networkHandler.isOnline()){
            startHomeActivity()
        }
    }
    private fun checkLoggedInUser(view: View){
        val firebase = Firebase.auth.currentUser
        if(firebase==null){
            checkAppThemeMode()
            setLocalLanguage()
            navigateLoginFragment(view)
        }
        else{
            getUserFromFirestore(firebase.uid, { docSnapshot->
                checkNetworkStatus()
               //startHomeActivity()
                val user = docSnapshot.toObject(AppUser::class.java)
                DataUtils.USER=user
            }) {
                navigateLoginFragment(view)
                Toast.makeText(requireContext(),"can't login",Toast.LENGTH_SHORT).show()
            }
        }
    }
    /*private var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        super.onAttach(requireActivity())
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }*/
    private fun startHomeActivity(){
        val intent = Intent(requireContext(),HomeActivity::class.java)
        startActivity(intent)
    }

    private fun navigateLoginFragment(view: View) {
        view.findNavController().navigate(R.id.login)
    }

}