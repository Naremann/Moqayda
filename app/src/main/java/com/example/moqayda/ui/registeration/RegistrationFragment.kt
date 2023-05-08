package com.example.moqayda.ui.registeration
import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentRegisterationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class RegistrationFragment : BaseFragment<FragmentRegisterationBinding, RegisterViewModel>(),Navigator,
    AdapterView.OnItemSelectedListener {

    private var city : String?=null
    @SuppressLint("Recycle", "UseCompatLoadingForDrawables", "WrongThread")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
        subscribeToLiveData()
        viewModel.navigator=this
        viewDataBinding.spinner.onItemSelectedListener = this
        initSpinner()

        val drawableId = R.drawable.user_img
        val resources: Resources = requireContext().resources
        val drawable: Drawable? = resources.getDrawable(drawableId, null)
        val bitmap: Bitmap = (drawable as? BitmapDrawable)?.bitmap
            ?: BitmapFactory.decodeResource(resources, drawableId)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val requestBody: RequestBody = RequestBody.create("image/jpeg".toMediaType(), outputStream.toByteArray())
        val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)

        viewModel.filePart = multipartBody

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




    override fun initViewModeL(): RegisterViewModel {
        return ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_registeration
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun navigateToLoginFragment() {
        requireView().findNavController().navigate(R.id.login)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        city=viewDataBinding.spinner.selectedItem.toString()
        viewModel.city= city as String
        Log.e("onItemSelected","city $city")
        Log.e("onItemSelected","cityViewModel ${viewModel.city}")

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}