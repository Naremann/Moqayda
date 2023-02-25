package com.example.moqayda.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<N> : ViewModel() {
    val messageLiveData = MutableLiveData<String>()
    val showLoading = MutableLiveData<Boolean>()
}