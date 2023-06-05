package com.example.moqayda.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.moqayda.HomeActivity
import com.example.moqayda.R
import com.example.moqayda.ui.chatRequests.RequestsFragment
import com.example.moqayda.ui.swa_public_offers.SwapOffersOfPublicItemsFragment
import com.example.moqayda.ui.swap_public_item_request.SwapPublicItemRequestFragment
import java.util.*
import javax.inject.Inject

class MyNotificationManager @Inject constructor(private val mCtx: Application) {


    fun textNotification(title: String?, message: String?, actionDestinationId: String?) {
        val rand = Random()
        val idNotification = rand.nextInt(1000000000)
        var destination = 0
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = mCtx.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "Channel_id_default", "Channel_name_default", NotificationManager.IMPORTANCE_HIGH
            )
            val attributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            notificationChannel.description = "Channel_description_default"
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.setSound(soundUri, attributes)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        if (actionDestinationId == "chat") {
            destination = R.id.requestFragment
        } else if (actionDestinationId == "swapOffer") {
            destination = R.id.swapOffersOfPublicItemsFragment
        }


        val notificationBuilder = NotificationCompat.Builder(mCtx, "Channel_id_default")
        val pendingIntent = NavDeepLinkBuilder(mCtx).setComponentName(HomeActivity::class.java)
            .setGraph(R.navigation.nav_graph_home)
            .setDestination(destination)
            .createPendingIntent()
        notificationBuilder.setAutoCancel(true)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher_moqayda)
            .setTicker(mCtx.resources.getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
        notificationManager.notify(idNotification, notificationBuilder.build())
    }
}