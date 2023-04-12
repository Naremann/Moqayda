package com.example.moqayda.ui.profile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.base.BaseViewModel

class ProfileViewModel: BaseViewModel<Navigator>() {
    val fullName = DataUtils.USER?.firstName+" "+DataUtils.USER?.lastName
    val email = DataUtils.USER?.email
    val phone = DataUtils.USER?.phoneNumber
    val address = DataUtils.USER?.city+"-"+DataUtils.USER?.address
    val image = DataUtils.USER?.image
}