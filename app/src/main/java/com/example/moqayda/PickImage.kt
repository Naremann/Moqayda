package com.example.moqayda

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat

private var permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
fun pickImage(context: Context, activity: Activity, resultLauncher: ActivityResultLauncher<Intent>) {
    if (hasPermissions(context, permissions)) {
        selectImage(resultLauncher)
    } else {
        requestLocationPermission(context,activity,resultLauncher)
    }
}

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }


    private fun requestLocationPermission(context: Context,activity:Activity,resultLauncher: ActivityResultLauncher<Intent>) {
        if (hasPermissions(context, permissions)) {
            selectImage(resultLauncher)

        }
        ActivityCompat.requestPermissions(
            activity,
            permissions,
            1
        )
    }
fun selectImage(resultLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    resultLauncher.launch(intent)

}

