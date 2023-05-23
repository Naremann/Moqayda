package com.example.moqayda.notification

import android.util.Log
import com.example.moqayda.api.fcm.Retrofit
import com.example.moqayda.models.Notification
import com.example.moqayda.repo.product.Resource

class Notifications {

    suspend fun sendNotification(notificationModel: Notification): Resource<Unit> {

        return try {
            val response = Retrofit.retrofitService.sendNotification(notificationModel)
            if (response.isSuccessful) {
                Log.e("TAG", "Notification in Kotlin: ${response.body()} ")
                Resource.Success(Unit)
            } else {
                Log.e("TAG", "Notification in Kotlin1: ${response.errorBody()} ")
                Resource.Error(response.message())

            }
        } catch (e: Exception) {
            Log.d("TAG", "Notification in Kotlin2: ${e.message} ")
            Resource.Error(e.localizedMessage)


        }

    }

}