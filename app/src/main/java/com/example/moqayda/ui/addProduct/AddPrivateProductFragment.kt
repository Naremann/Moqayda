package com.example.moqayda.ui.addProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moqayda.databinding.FragmentAddPrivateProductBinding

class AddPrivateProductFragment : Fragment() {
    lateinit var binding:FragmentAddPrivateProductBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentAddPrivateProductBinding.inflate(inflater,container,false)
        return binding.root
    }

}