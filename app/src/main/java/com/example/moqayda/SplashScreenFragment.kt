package com.example.moqayda

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.moqayda.database.getUserFromFirestore
import com.example.moqayda.databinding.FragmentSplashScreenBinding
import com.example.moqayda.models.AppUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SplashScreenFragment : androidx.fragment.app.Fragment() {
    private var binding : FragmentSplashScreenBinding?=null

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
    private fun checkLoggedInUser(view: View){
        val firebase = Firebase.auth.currentUser
        if(firebase==null){
            navigateLoginFragment(view)
        }
        else{
            getUserFromFirestore(firebase.uid, { docSnapshot->
                startHomeActivity()
                val user = docSnapshot.toObject(AppUser::class.java)
                DataUtils.USER=user
            }) {
                navigateLoginFragment(view)
                Toast.makeText(requireContext(),"can't login",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun startHomeActivity(){
        val intent = Intent(requireContext(),HomeActivity::class.java)
        startActivity(intent)
    }

    private fun navigateLoginFragment(view: View) {
        view.findNavController().navigate(R.id.login)
    }

}