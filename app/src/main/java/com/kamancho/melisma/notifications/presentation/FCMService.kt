package com.kamancho.melisma.notifications.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.util.UnstableApi
import com.kamancho.melisma.R
import com.kamancho.melisma.main.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@UnstableApi /**
 * Created by HP on 23.04.2023.
 **/
class FCMService: FirebaseMessagingService() {

    companion object{
        private const val notification_channel_update_id = "notification_channel_update_id"
        private const val notification_channel_update_name = "Update"
    }


    override fun onNewToken(token: String) = Unit

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationChannel = NotificationChannel(
            notification_channel_update_id,
            notification_channel_update_name,
            NotificationManager.IMPORTANCE_HIGH )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)

        val notification = Notification.Builder(this, notification_channel_update_id)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setSmallIcon(R.drawable.tone)
            .setContentIntent(
                PendingIntent.getActivities(
                    this,
                    0,
                    arrayOf(Intent(this,MainActivity::class.java)),
                    PendingIntent.FLAG_IMMUTABLE))
            .build()

        notificationManager.notify(1,notification)

    }
}