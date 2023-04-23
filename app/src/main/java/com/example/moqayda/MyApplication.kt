package com.example.moqayda

import android.app.Application
import android.net.ConnectivityManager
import com.example.moqayda.database.local.MyDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
        MyDatabase.init(context = this)
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        Constants.ConnectivityManager=connectivityManager
    }
}