package com.example.moqayda.ui.chat

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentChatBinding
import com.example.moqayda.models.AppUser
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {

    private lateinit var adapter: UserAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility = VISIBLE
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = VISIBLE
        activity?.findViewById<FloatingActionButton>(R.id.fabButton)?.visibility = GONE

        initRecyclerView()




    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): ChatViewModel {
        return ViewModelProvider(this)[ChatViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_chat
    }

    private fun initRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        viewDataBinding.chatRecyclerView.layoutManager = layoutManager
    }

}



