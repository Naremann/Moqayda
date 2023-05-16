package com.example.moqayda.ui.add_private_product

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentAddPrivateProductBinding
import com.example.moqayda.pickImage
import com.example.moqayda.selectImage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.BuildConfig

@AndroidEntryPoint
class AddPrivateProductFragment :
    BaseFragment<FragmentAddPrivateProductBinding, AddPrivateProductViewModel>(),Navigator {
    private var selectedFile: Uri? = null
    private lateinit var permReqLauncher: ActivityResultLauncher<Array<String>>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideBottomAppBar()
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator=this
        viewDataBinding.viewModel=viewModel
        viewDataBinding.lifecycleOwner = this

        initPermReqLauncher()
        observeToLiveData()
        subscribeToLiveData()

        viewDataBinding.pickButton.setOnClickListener {
             pickImage(requireContext(), requireActivity(), resultLauncher)
        }
        viewDataBinding.deleteImage.setOnClickListener {
            deleteImage()
        }
    }

    private fun observeToLiveData() {
        viewModel.toastMessage.observe(viewLifecycleOwner){message->
            showToastMessage(message)

        }

        viewModel.imageUri.observe(viewLifecycleOwner){ imageUri ->
            if (imageUri != null){
                showImage()
            }else{
                hideImage()
            }
        }
    }

    private fun showImage() {
        viewDataBinding.pickButton.visibility = View.GONE
        viewDataBinding.productImage.visibility = View.VISIBLE
        viewDataBinding.deleteImage.visibility = View.VISIBLE
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                viewDataBinding.pickButton.visibility = View.GONE
                viewDataBinding.productImage.visibility = View.VISIBLE
                viewDataBinding.deleteImage.visibility = View.VISIBLE
                result.data!!.data?.let { viewModel.setImageUri(it) }
                selectedFile = result.data!!.data!!




            }

        }

    private fun deleteImage() {
        hideImage()
        selectedFile = null
    }

    private fun hideImage() {
        viewDataBinding.productImage.visibility = View.GONE
        viewDataBinding.deleteImage.visibility = View.GONE
        viewDataBinding.pickButton.visibility = View.VISIBLE
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Snackbar.make(
                viewDataBinding.addProductLayout,
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
            selectImage(resultLauncher)
        }

    }
    private fun initPermReqLauncher() {
        permReqLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all { it.value }
                if (granted) {
                    selectImage(resultLauncher)
                } else {
                    Snackbar.make(
                        viewDataBinding.addProductLayout,
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

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): AddPrivateProductViewModel {
        return ViewModelProvider(this)[AddPrivateProductViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_private_product
    }

    override fun navigateToPrivateProductFragment() {
        showAlertDialog(getString(R.string.product_uploaded),getString(R.string.ok),{ _, _ ->
            findNavController().navigate(AddPrivateProductFragmentDirections.actionAddPrivateProductFragmentToPrivateProductsFragment(false,null))
        })
    }

}