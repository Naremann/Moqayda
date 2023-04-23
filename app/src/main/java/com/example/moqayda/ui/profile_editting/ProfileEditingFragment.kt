package com.example.moqayda.ui.profile_editting

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.moqayda.*
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.database.downloadFirebaseStorageImage
import com.example.moqayda.databinding.FragmentProfileEdittingBinding
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.BuildConfig


class ProfileEditingFragment : BaseFragment<FragmentProfileEdittingBinding, ProfileEditingViewModel>(), AdapterView.OnItemSelectedListener {
    private var city : String?=null
    private var selectedFile: Uri? = null
    var permReqLauncher: ActivityResultLauncher<Array<String>>?=null
    private var resultLauncher:ActivityResultLauncher<Intent>?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPermReqLauncher()
        observeToLiveData()
        subscribeToLiveData()
        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,getString(R.string.edit_profile),this)
        viewDataBinding.vm=viewModel
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.spinner.onItemSelectedListener = this
        initResultLauncher()
        initSpinner()
        showUserImage()
        viewDataBinding.addIcon.setOnClickListener {
            resultLauncher?.let { it1 -> pickImage(requireContext(),requireActivity(), it1)
                showUserImage()
            }
        }
        viewDataBinding.deleteImage.setOnClickListener {
            hideImage()
        }

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

    private fun initResultLauncher(){
        resultLauncher=
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == Activity.RESULT_OK) {

                    result.data!!.data?.let { viewModel.setImageUri(it) }
                    Log.e("initResult","uri ${result.data?.data}")
                    selectedFile = result.data!!.data!!
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,selectedFile
                        )
                    viewDataBinding.userImage.setImageBitmap(bitmap)
                }
            }

    }
    private fun showUserImage(){
        DataUtils.USER?.id?.let {
            downloadFirebaseStorageImage({ uri->
                viewDataBinding.deleteImage.isVisible=true
                Glide.with(viewDataBinding.userImage.context).load(uri).into(viewDataBinding.userImage)
            }, { ex->
                ex.localizedMessage?.let { it1 -> showToastMessage(it1) }
            }, it)
        }
    }

    private fun observeToLiveData() {
        viewModel.isDeletedImage.observe(viewLifecycleOwner){isDeleted->
            if(isDeleted==true){
                hideImage()
            }
        }
        viewModel.toastMessage.observe(viewLifecycleOwner){message->
            showToastMessage(message)

        }
        viewModel.isShowProgressDialog.observe(viewLifecycleOwner){isShowProgress->
            if(isShowProgress==true)
                showProgressDialog()
            else{
                hideProgressDialog()
            }

        }
        viewModel.message.observe(viewLifecycleOwner){message->
            showAlertDialog(message,getString(R.string.ok), { dialog, which ->
                val navController = findNavController()
                navController.run {
                    popBackStack()
                    navigate(R.id.profileFragment)
                }
            })
        }
        viewModel.isUploadedImage.observe(viewLifecycleOwner){isUploaded->
            if(isUploaded==true){
                showUserImage()
            }

        }
    }

    private fun hideImage() {
        selectedFile=null
        viewDataBinding.deleteImage.isVisible=false
        viewModel.setImageUri(null)
        viewDataBinding.userImage.setImageResource(R.drawable.ic_person)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Snackbar.make(
                viewDataBinding.root,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.settings) {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        } else {
            resultLauncher?.let { selectImage(it) }
        }

    }
    private fun initPermReqLauncher() {
        permReqLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all { it.value }
                if (granted) {
                    resultLauncher?.let { selectImage(it) }
                } else {
                    Snackbar.make(
                        viewDataBinding.root,
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(R.string.settings) {
                            startActivity(Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            })
                        }.show()
                }
            }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        city=viewDataBinding.spinner.selectedItem.toString()
        viewModel.city= city as String
        Log.e("onItemSelected","city $city")
        Log.e("onItemSelected","cityViewModel ${viewModel.city}")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): ProfileEditingViewModel {
        return ViewModelProvider(this)[ProfileEditingViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile_editting
    }
}