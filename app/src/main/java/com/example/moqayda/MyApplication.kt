package com.example.moqayda

import android.app.Application
import android.net.ConnectivityManager
import com.example.moqayda.database.MyDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MyDatabase.init(context = this)
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        Constants.ConnectivityManager=connectivityManager
    }
}