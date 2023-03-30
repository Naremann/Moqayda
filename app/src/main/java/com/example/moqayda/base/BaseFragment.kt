package com.example.moqayda.base

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<DB: ViewDataBinding,VM: BaseViewModel<*>> : Fragment() {
    lateinit var viewDataBinding: DB
    lateinit var viewModel: VM
    var alertDialog: AlertDialog?=null
    lateinit var progressDialog : ProgressDialog
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
        Toast.makeText(requireContext(),message, duration).show()
    }
    private fun showAlertDialog(
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
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }
    fun hideProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss()
        }

    }

}