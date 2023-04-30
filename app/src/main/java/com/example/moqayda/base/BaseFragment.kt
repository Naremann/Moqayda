package com.example.moqayda.base

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.moqayda.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

abstract class BaseFragment<DB: ViewDataBinding,VM: BaseViewModel<*>> : Fragment() {
    lateinit var viewDataBinding: DB
    lateinit var viewModel: VM
    var alertDialog: AlertDialog?=null
    private var progressDialog : ProgressDialog?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(layoutInflater,getLayoutId(),container,false)
        viewModel = initViewModeL()
        return getViews()
    }

    abstract fun getViews():View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLayoutId()


    }

    fun subscribeToLiveData(){
        viewModel.messageLiveData.observe(this) { message ->
            showAlertDialog(message,"Ok")
        }
        viewModel.showLoading.observe(this) { loadingMessage ->
            if(loadingMessage == true){
                showProgressDialog()
            }
            else{
                hideProgressDialog()
            }
        }
    }

    abstract fun initViewModeL() : VM
    abstract fun getLayoutId() : Int

    fun showToastMessage(message:String,duration:Int=Toast.LENGTH_LONG){
        Toast.makeText(getViews().context,message, duration).show()
    }
    fun showAlertDialog(
        message:String, posActionName:String?=null,
        posActionListener: DialogInterface.OnClickListener?=null,
        negActionName : String?=null,
        negActionListener: DialogInterface.OnClickListener?=null,
        cancellable: Boolean = true
    ){
        var builder = AlertDialog.Builder(requireContext())
            .setMessage(message)
        if(posActionName != null){
            builder.setPositiveButton(posActionName, posActionListener)
        }
        if(negActionName != null){
            builder.setNegativeButton(negActionName,negActionListener)
        }
        builder.setCancelable(cancellable)
        alertDialog = builder.show()
    }
    fun showProgressDialog(){
        progressDialog = ProgressDialog(requireContext())
        progressDialog?.setMessage("Loading....")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }
    fun hideProgressDialog(){
        progressDialog?.dismiss()
    }

    fun hideBottomAppBar(){
        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE
        activity?.findViewById<FloatingActionButton>(R.id.fabButton)?.hide()
        val fragmentContainerView: View? = activity?.findViewById(R.id.home_nav_host_fragment)
        val layoutParams = fragmentContainerView?.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0,0,0,0)
    }

    fun showBottomAppBar(){
        val actionBarSizeAttr = android.R.attr.actionBarSize
        val typedValue = TypedValue()
        val actionBarSize = if (context?.theme?.resolveAttribute(actionBarSizeAttr, typedValue, true) == true) {
            TypedValue.complexToDimensionPixelSize(
                typedValue.data,
                requireContext().resources.displayMetrics
            )
        } else {
            70
        }

        val fragmentContainerView: View? = activity?.findViewById(R.id.home_nav_host_fragment)
        val layoutParams = fragmentContainerView?.layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.setMargins(0,0,0,actionBarSize)
        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility = View.VISIBLE
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
        activity?.findViewById<FloatingActionButton>(R.id.fabButton)?.show()


    }

    fun hideFloatingBtn(){
        activity?.findViewById<FloatingActionButton>(R.id.fabButton)?.hide()
    }

    fun showFloatingBtn(){
        activity?.findViewById<FloatingActionButton>(R.id.fabButton)?.show()
    }

}