package com.example.moqayda.ui.notification

import com.example.moqayda.models.Notification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Api {

    companion object {
        const val BASE_URL = "https://fcm.googleapis.com"
    }

    @Headers(
        "Authorization: key=AAAAPiiT6SU:APA91bGtzQK7PXIVIT62KP48Zn4-fixkAlq8t1opvzpKt3kUmak3X-iCBTPK-Zgpeyf3PV7L5-epSlhzGfbyR5F9NsyDwvIudfUKX9sKBh5nRJZVJdFbw5vbq0FqwQJnG06z3UANYHmO"
        ,
        "Content-Type:application/json"
    )

    @POST("/fcm/send")
    suspend fun sendNotification( @Body notificationModel: Notification): Response<ResponseBody>

}