package com.example.musicapp.player.presentation

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.media.session.MediaSession
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.musicapp.R

/**
 * Created by HP on 31.01.2023.
 **/
sealed interface PlayerServiceState{

    fun apply(
        //mediaSession: MediaSessionCompat,
        mediaPlayer: MediaPlayer,
        notificationBuilder: NotificationCompat.Builder,
        notificationManager: NotificationManagerCompat,
        context: Context
    )

    data class Play(
        private val trackUrl: String,
        private val trackName: String,
        private val authorName:String
    ): PlayerServiceState{

        @SuppressLint("MissingPermission")
        override fun apply(
           // mediaSession: MediaSessionCompat,
            mediaPlayer: MediaPlayer,
            notificationBuilder: NotificationCompat.Builder,
            notificationManager: NotificationManagerCompat,
            context: Context
        ) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(trackUrl)
            mediaPlayer.prepare()

//            mediaSession.setMetadata(
//                MediaMetadataCompat.Builder()
//                    .putString(MediaMetadata.METADATA_KEY_TITLE, trackName)
//                    .putString(MediaMetadata.METADATA_KEY_ARTIST,authorName)
//                    .putString(MediaMetadata.METADATA_KEY_MEDIA_URI,trackUrl)
//                    .build()
//            )
            notificationManager.notify(1,notificationBuilder
//                .addAction(
//                    R.drawable.play_btn_selector,
//                    "Pause",
//                    PendingIntent.getBroadcast(
//                        context,
//                        0,
//                        Intent(context, PlayerBroadcastReceiver::class.java)
//                            .putExtra("action","pause"),
//                         PendingIntent.FLAG_ONE_SHOT
//                ))
                .build()
            )
            mediaPlayer.start()
        }

    }

    object Pause: PlayerServiceState{

        @SuppressLint("MissingPermission")
        override fun apply(
           // mediaSession: MediaSessionCompat,
            mediaPlayer: MediaPlayer,
            notificationBuilder: NotificationCompat.Builder,
            notificationManager: NotificationManagerCompat,
            context: Context
        ) {
            mediaPlayer.pause()
            notificationManager.notify(1,
            notificationBuilder
//                .addAction(R.drawable.play,"Pause",PendingIntent.getBroadcast(context,0, Intent(context,
//                    PlayerBroadcastReceiver::class.java).putExtra("action","play"),PendingIntent.FLAG_ONE_SHOT
//                ))
                .build()
            )

        }

    }

    object Resume: PlayerServiceState{

        override fun apply(
          //  mediaSession: MediaSessionCompat,
            mediaPlayer: MediaPlayer,
            notificationBuilder: NotificationCompat.Builder,
            notificationManager: NotificationManagerCompat,
            context: Context
        ) {
            mediaPlayer.start()

        }

    }

    object Disabled: PlayerServiceState{
        override fun apply(
           // mediaSession: MediaSessionCompat,
            mediaPlayer: MediaPlayer,
            notificationBuilder: NotificationCompat.Builder,
            notificationManager: NotificationManagerCompat,
            context: Context
        ) {
            mediaPlayer.stop()

        }


    }
}
