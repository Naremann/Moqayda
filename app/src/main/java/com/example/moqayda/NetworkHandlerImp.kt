package com.example.moqayda

import android.app.Application
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService

class NetworkHandlerImp:NetworkHandler {
    override fun isOnline(): Boolean {
        return isNetworkAvailable()
    }
    private fun isNetworkAvailable() : Boolean {
        var connected = false
        if(Constants.ConnectivityManager?.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
            Constants.ConnectivityManager!!.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED)
            connected = true
        return connected
    }
}