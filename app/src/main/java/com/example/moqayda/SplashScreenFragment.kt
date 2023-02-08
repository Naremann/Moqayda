package com.example.moqayda

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.moqayda.databinding.FragmentSplashScreenBinding


class SplashScreenFragment : androidx.fragment.app.Fragment() {
    private var binding : FragmentSplashScreenBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({ navigateLoginFragment(view) },3000)

    }

    private fun navigateLoginFragment(view: View) {
        view.findNavController().navigate(R.id.login)
    }

}