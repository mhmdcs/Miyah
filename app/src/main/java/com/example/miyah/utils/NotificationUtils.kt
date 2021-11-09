package com.example.miyah.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.miyah.R

private val NOTIFICATION_ID = 0


//this is an extension function
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

// To support devices running older versions you need to use NotificationCompat builder instead of notification builder.
// Get an instance of the NotificationCompat builder, pass in the app context and a channel id.
// The channel id is a string value from string resources which uses the matching channel.
// Starting with API level 26, all notifications must be assigned to a channel.
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.water_tank_notification_channel_id)
    )

//Set the notification icon to represent your app, title and the content text for the message
//you want to give to the user.
        .setSmallIcon(R.drawable.miyah_logo)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

//call notify() with a unique id for your notification and with the Notification object from your builder (we named it "builder")
// This id represents the current notification instance and is needed for updating or canceling this
//notification. Since your app will only have one active notification at a given time, you can use
//the same id for all your notifications. You are already given a constant for this purpose called NOTIFICATION_ID in NotificationUtils.kt.
// Notice we can directly call notify() since you are performing the call from an extension function on the same class.
    notify(NOTIFICATION_ID,builder.build())

}