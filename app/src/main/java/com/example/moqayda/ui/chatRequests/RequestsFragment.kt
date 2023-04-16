package com.example.moqayda.ui.chatRequests

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentRequestsBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RequestsFragment : BaseFragment<FragmentRequestsBinding, RequestViewModel>() {
    private val requestsViewModel by activityViewModels<RequestViewModel>()

    private lateinit var adapter: ReqMessageAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBottomAppBar()
        hideFloatingBtn()

        initRecyclerView()

        requestsViewModel.req.observe(viewLifecycleOwner){
            adapter = ReqMessageAdapter(it,requestsViewModel,requireContext())
            viewDataBinding.chatRecyclerView.adapter = adapter
        }


    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): RequestViewModel {
        return ViewModelProvider(this)[RequestViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_requests
    }

    private fun initRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        viewDataBinding.chatRecyclerView.layoutManager = layoutManager
    }

}



