package com.example.moqayda.ui.addProduct

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.example.moqayda.*
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentAddProductBinding
import com.example.moqayda.models.test.CategoryItem
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.io.File


class AddProductFragment : BaseFragment<FragmentAddProductBinding, AddProductViewModel>(),
    Navigator {
    private lateinit var permReqLauncher: ActivityResultLauncher<Array<String>>
    private var selectedFile: Uri? = null
    private lateinit var categoryList: List<CategoryItem>
    //private lateinit var selectedCategory: CategoryItem
    private lateinit var pathImage : String
    private var backgroundColor :Int=0
    private var categoryId:Int=0

    private var permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityBottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        val activityBottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)


        activityBottomAppBar?.visibility = GONE
        activityBottomNavigationView?.visibility = GONE

        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = this
        observeToLiveData()
        initPermReqLauncher()
        viewModel.navigator = this



        pathImage=AddProductFragmentArgs.fromBundle(requireArguments()).pathImage
        backgroundColor=AddProductFragmentArgs.fromBundle(requireArguments()).backgroundColor
        categoryId=AddProductFragmentArgs.fromBundle(requireArguments()).id
        //selectedCategory = CategoryItem()

        //viewDataBinding.category = selectedCategory
        bindImage(viewDataBinding.categoryItemImg, pathImage)
        viewDataBinding.categoryItemImg.setBackgroundColor(
            getColor(requireContext(), backgroundColor)
        )

        viewDataBinding.pickButton.setOnClickListener {
            pick()

        }
        viewDataBinding.uploadButton.setOnClickListener {
            upload()
        }
        viewDataBinding.deleteImage.setOnClickListener {
            viewDataBinding.productImage.visibility = GONE
            viewDataBinding.deleteImage.visibility = GONE
            viewDataBinding.pickButton.visibility = VISIBLE
            selectedFile = null
        }
        viewDataBinding.categoryItemImg.setOnClickListener {
            viewModel.navigateToSelectCategory()
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
        viewModel.response.observe(viewLifecycleOwner, Observer { response ->
            if (response.isNotEmpty()) {
                if (response == resources.getString(R.string.product_uploaded)) {
                    Toast.makeText(requireContext(), R.string.product_uploaded, Toast.LENGTH_LONG)
                        .show()
                    File(requireContext().cacheDir, viewModel.fileName.value.toString()).delete()
                    viewModel.navigateToHome()
                } else {
                    Toast.makeText(requireContext(), R.string.upload_failed, Toast.LENGTH_LONG)
                        .show()
                }

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
                viewDataBinding.pickButton.visibility = GONE
                viewDataBinding.productImage.visibility = VISIBLE
                viewDataBinding.deleteImage.visibility = VISIBLE
                viewDataBinding.productImage.setImageURI(result.data!!.data)
                selectedFile = result.data!!.data!!
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
        if (selectedFile != null) {
            if (getFileSize(requireContext(), selectedFile!!) < getAvailableInternalMemorySize()) {

                val cursor = requireContext().applicationContext.contentResolver.query(
                    selectedFile!!,
                    null,
                    null,
                    null,
                    null
                )
                cursor?.moveToFirst()

                // Check if the DISPLAY_NAME column is present in the Cursor
                val displayNameIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val selectedImageName = if (displayNameIndex != null && displayNameIndex >= 0) {
                    cursor.getString(displayNameIndex)
                } else {
                    // Set the file name to a default value or display an error message
                    "Unknown file name"
                }
                cursor?.close()

                if (selectedImageName != null) {
                    viewModel.upload(
                        categoryId.toString(),
                        selectedFile!!,
                        getFilePathFromUri(requireContext(), selectedFile!!, viewModel,selectedImageName)
                    )
                }else{
                    Toast.makeText(requireContext(),"No image Selected",Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(requireContext(), R.string.no_enough_space, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), R.string.choose_image, Toast.LENGTH_LONG).show()
        }


    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }


    private fun requestLocationPermission() {
        if (hasPermissions(requireContext(), permissions)) {
            selectImage()

        }
        ActivityCompat.requestPermissions(
            requireActivity(),
            permissions,
            1
        )
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
            selectImage()
        }

    }

    override fun onNavigateToHomeFragment() {
        findNavController(viewDataBinding.addProductLayout).navigate(R.id.homeFragment)
    }

    override fun onNavigateToSelectCategoryFragment() {
        findNavController(viewDataBinding.addProductLayout).navigate(R.id.selectCategoryFragment)
    }
}