package com.example.moqayda.ui.addProduct

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moqayda.*
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentAddProductBinding
import com.example.moqayda.models.CategoryItem
import com.google.android.material.snackbar.Snackbar
import java.io.File


class AddProductFragment : BaseFragment<FragmentAddProductBinding, AddProductViewModel>() {
    private lateinit var permReqLauncher: ActivityResultLauncher<Array<String>>
    private var selectedFile: Uri? = null
    private lateinit var categoryList: List<CategoryItem>
    private lateinit var selectedCategory: CategoryItem

    private var permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = this
        observeToLiveData()
        initPermReqLauncher()

        viewDataBinding.categoriesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategory = categoryList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }


        viewDataBinding.pickButton.setOnClickListener {
            pick()
        }
        viewDataBinding.uploadButton.setOnClickListener {
            upload()
        }
    }

    private fun initPermReqLauncher() {
        permReqLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all { it.value }
                if (granted) {
                    selectImage()
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

    private fun observeToLiveData() {
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                File(requireContext().cacheDir, viewModel.fileName.value.toString()).delete()
                viewModel.reset()
            }
        })

        viewModel.connectionError.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        })
        viewModel.categoryList.observe(viewLifecycleOwner, Observer {
            categoryList = it
            initSpinner(categoryList)
        })
        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
        viewModel.descriptionHelperText.observe(viewLifecycleOwner, Observer {
            viewDataBinding.textInputProductDescription.helperText = it
        })
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): AddProductViewModel {
        val addProductViewModelFactory = AddProductViewModelFactory(requireContext())

        return ViewModelProvider(this, addProductViewModelFactory)[AddProductViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_product
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                selectedFile = result.data!!.data!!
                viewDataBinding.pathImage.text = selectedFile!!.path.toString()
            }

        }

    private fun pick() {
        if (hasPermissions(requireContext(), permissions)) {
            selectImage()
        } else {
            requestLocationPermission()
        }
    }

    private fun upload() {

        if (getFileSize(requireContext(), selectedFile!!) < getAvailableInternalMemorySize()) {
            if (selectedFile != null) {
                viewModel.upload(
                    selectedCategory,
                    selectedFile!!,
                    getFilePathFromUri(requireContext(), selectedFile!!, viewModel)
                )
            } else {
                Toast.makeText(requireContext(), "Please Choose a photo", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "No enough space", Toast.LENGTH_LONG).show()
        }


    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun initSpinner(categoryList: List<CategoryItem>) {
        viewDataBinding.categoriesSpinner.adapter = SpinnerAdapter(requireContext(), categoryList)
    }


    private fun permissionApproved(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        if (permissionApproved()) {
            selectImage()

        }
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED){
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
        }else{
            selectImage()
        }

    }
}