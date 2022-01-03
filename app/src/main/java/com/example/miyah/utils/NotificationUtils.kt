package com.example.miyah.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.miyah.R

private const val NOTIFICATION_ID = 0


//kotlin extension function to send notifications
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.water_tank_notification_channel_id)
    )
        .setSmallIcon(R.drawable.miyah_logo)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
    notify(NOTIFICATION_ID,builder.build())
}


