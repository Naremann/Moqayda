package com.example.moqayda.ui.profile_editting

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.moqayda.*
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.database.getFirebaseImageUri
import com.example.moqayda.database.getUerImageFromFirebase
import com.example.moqayda.databinding.FragmentProfileEdittingBinding
import com.example.moqayda.models.AppUser
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody


class ProfileEditingFragment : BaseFragment<FragmentProfileEdittingBinding, ProfileEditingViewModel>(), AdapterView.OnItemSelectedListener,Navigator {
    private var city : String?=null
    private var selectedFile: Uri? = null
    private var permReqLauncher: ActivityResultLauncher<Array<String>>?=null
    private var resultLauncher:ActivityResultLauncher<Intent>?=null
    var imageUri:Uri?=null
    private lateinit var currentUser: AppUser
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPermReqLauncher()
        observeToLiveData()
        subscribeToLiveData()
        viewModel.navigator = this
        currentUser = ProfileEditingFragmentArgs.fromBundle(requireArguments()).currentUser
        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,getString(R.string.edit_profile),this)
        viewDataBinding.vm=viewModel
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.spinner.onItemSelectedListener = this
        initResultLauncher()
        initSpinner()
        viewDataBinding.addIcon.setOnClickListener {
            resultLauncher?.let { it1 -> pickImage(requireContext(),requireActivity(), it1)
            }
        }
        viewDataBinding.deleteImageTv.setOnClickListener {
            hideImage()
        }
        loadUserImage()
        checkSelectedImage()



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

    private fun initResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data!!.data?.let { viewModel.setImageUri(it) }
                    Log.e("initResult", "uri ${result.data?.data}")
                    selectedFile = result.data!!.data!!
                    viewModel.setSelectedImageUri(selectedFile)
                }
            }
    }
    private fun loadUserImage(){
        DataUtils.USER?.id?.let {userId->
            getUerImageFromFirebase(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    DataUtils.USER!!.id?.let {
                        getFirebaseImageUri({ uri->
                            imageUri=uri
                            viewDataBinding.progressBar.isVisible=false
                            viewDataBinding.deleteImageTv.isVisible=true
                            Picasso.with(viewDataBinding.userImage.context).load(uri).into(viewDataBinding.userImage)

                        }, {ex->
                            ex.localizedMessage?.let { error -> showToastMessage(error) }
                            viewDataBinding.progressBar.isVisible=false

                        }, userId)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToastMessage("Error Loading Image")
                }
            }, userId)
        }

    }

    private fun checkSelectedImage(){
        if (selectedFile !=null){
            viewModel.setSelectedImageUri(selectedFile)
        }else{
            Log.e("ProfileEditingFragment",currentUser.image!!)
            viewModel.setSelectedImageUrl(currentUser.image)
        }

    }

    private fun observeToLiveData() {
        viewModel.isUploadedImage.observe(viewLifecycleOwner){isUploaded->
            if(isUploaded==true)
                viewDataBinding.deleteImageTv.isVisible=true

        }
        viewModel.isDeletedImage.observe(viewLifecycleOwner){isDeleted->
            if(isDeleted==true){
                hideImage()
            }
        }
        viewModel.toastMessage.observe(viewLifecycleOwner){message->
            showToastMessage(message)

        }
        viewModel.message.observe(viewLifecycleOwner){message->
            showAlertDialog(message,getString(R.string.ok), { _, _ ->
                val navController = findNavController()
                navController.run {
                    popBackStack()
                    navigate(R.id.profileFragment)
                }
            })
        }


    }

    private fun hideImage() {
        selectedFile=null
        viewDataBinding.deleteImageTv.isVisible=false
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
        val viewModelFactory = ProfileEditingViewModelFactory(requireContext())
        return ViewModelProvider(this,viewModelFactory)[ProfileEditingViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile_editting
    }

    override fun onNavigateToProfileFragment() {
        this.findNavController().navigate(R.id.profileFragment)
    }
}