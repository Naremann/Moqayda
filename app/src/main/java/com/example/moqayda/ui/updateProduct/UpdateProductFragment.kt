package com.example.moqayda.ui.updateProduct

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.bindImageUri
import com.example.moqayda.databinding.FragmentUpdateProductBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.Product
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.BuildConfig
import kotlin.properties.Delegates

class UpdateProductFragment : BaseFragment<FragmentUpdateProductBinding, UpdateProductViewModel>(),
    Navigator {

    var auth: FirebaseAuth = Firebase.auth
    private lateinit var permReqLauncher: ActivityResultLauncher<Array<String>>
    private var selectedFile: Uri? = null
    private var permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    lateinit var selectedProduct: Product
    var selectedCategoryId by Delegates.notNull<Int>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding.toolbar.initToolbar(
            viewDataBinding.toolbar,
            requireContext().getString(R.string.edit_product),
            this
        )

        selectedProduct = UpdateProductFragmentArgs.fromBundle(requireArguments()).selectedProduct
        selectedCategoryId = UpdateProductFragmentArgs.fromBundle(requireArguments()).categoryId
        viewDataBinding.viewModel = viewModel
        viewDataBinding.product = selectedProduct
        viewModel.setProductDetails(selectedProduct)

        viewModel.imageUri.observe(viewLifecycleOwner) {
            bindImageUri(viewDataBinding.productImage, it)
        }

        viewModel.getCategory(selectedCategoryId)

        viewModel.category.observe(viewLifecycleOwner){
            viewDataBinding.category = it
        }
        viewModel.descriptionHelperText.observe(viewLifecycleOwner){
            viewDataBinding.textInputProductDescription.helperText = it
        }

        initPermReqLauncher()

        viewDataBinding.changeImage.setOnClickListener {
            pick()
        }

        viewDataBinding.categoryItemImg.setOnClickListener {
            viewModel.navigateToSelectCategory(selectedProduct, true)
        }


        checkSelectedImage()
        subscribeToLiveData()

    }


    private fun checkSelectedImage(){
        if (selectedFile !=null){
            viewModel.setImageUri(selectedFile!!)
        }else{
            viewModel.setSelectedImageUrl(selectedProduct.pathImage!!)
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

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                result.data!!.data?.let { viewModel.setImageUri(it) }
                selectedFile = result.data!!.data!!
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
        grantResults: IntArray,
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

    private fun pick() {
        if (hasPermissions(requireContext(), permissions)) {
            selectImage()
        } else {
            requestLocationPermission()
        }
    }


    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): UpdateProductViewModel {
        val viewModelFactory = UpdateProductViewModelFactory(requireContext())
        return ViewModelProvider(this, viewModelFactory)[UpdateProductViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_update_product
    }

    override fun onNavigateToSelectCategoryFragment(product: Product, isUpdate: Boolean) {
        findNavController().navigate(UpdateProductFragmentDirections.actionUpdateProductFragmentToSelectCategoryFragment(
            product,
            isUpdate))
    }

    override fun navigateToUserPublicItems() {
        this.findNavController()
            .navigate(UpdateProductFragmentDirections.actionUpdateProductFragmentToUserPublicItemsFragment(
                false,
                null))
    }
}