package com.example.musicapp.player.di

import android.app.Notification.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer

import androidx.core.app.NotificationManagerCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.example.musicapp.app.main.presentation.MainActivity
import com.example.musicapp.player.presentation.MediaSessionCallBack

import dagger.Module
import dagger.Provides

/**
 * Created by HP on 30.01.2023.
 **/
@Module
class PlayerModule {

    companion object{
        private const val CHANNEL_ID = "channel_player"
        private const val NOTIFICATION_NAME = "player_notification"
    }

    @Provides
    @PlayerServiceScope
    fun provideMediaPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

    @Provides
    @PlayerServiceScope
    fun providePendingIntent(context: Context): PendingIntent {
        return TaskStackBuilder.create(context).run {
            addNextIntent(Intent(context , MainActivity::class.java))
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    @Provides
    @PlayerServiceScope
    fun provideMediaSession(
        context: Context,
        player: ExoPlayer,
        pendingIntent: PendingIntent,
        callBack: MediaSessionCallBack
    ): MediaSession {
        return MediaSession.Builder(context, player)
            .setSessionActivity(pendingIntent)
            .setCallback(callBack)
            .build()
    }

//    @Provides
//    @PlayerServiceScope
//    fun provideMediaSession(context: Context): MediaSessionCompat{
//        val mediaSessionCompat = MediaSessionCompat(context, "tag")
//        mediaSessionCompat.setMetadata(
//            MediaMetadataCompat.Builder()
//                .putString(MediaMetadata.METADATA_KEY_TITLE, "my ttl")
//                .putString(MediaMetadata.METADATA_KEY_ARTIST,"my art")
//                .putString(MediaMetadata.METADATA_KEY_MEDIA_URI,"dsds")
//                .build()
//        )
//        mediaSessionCompat.setPlaybackState(PlaybackStateCompat.Builder()
//            .setState(PlaybackStateCompat.STATE_PLAYING, 0,1f)
//            //.addCustomAction()
//            .build())
//        mediaSessionCompat.setCallback(object :MediaSessionCompat.Callback(){
//            override fun onPlay() {
//                super.onPlay()
//                Log.d("tag", "onPlay: ")
//            }
//
//            override fun onPause() {
//                super.onPause()
//                Log.d("tag", "onPause: ")
//            }
//        })
//        mediaSessionCompat.setPlaybackState(PlaybackStateCompat.Builder().setState(
//            PlaybackStateCompat.STATE_PAUSED,0,1f).build())
//        return  mediaSessionCompat
//    }
//
//
//
//
//    @Provides
//    @PlayerServiceScope
//    fun provideNotification(
//        context: Context,
//        intent: PendingIntent,
//        mediaSessionCompat: MediaSessionCompat,
//    ): NotificationCompat.Builder {
//
//        mediaSessionCompat.isActive = true
//        return NotificationCompat.Builder(context, CHANNEL_ID )
//            .setAutoCancel(false)
//            .setOngoing(true)
//            .setSmallIcon(R.drawable.tone)
//            .setContentTitle("Track Name")
//            .setContentText("Author Name")
//            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
//            .setContentIntent(intent)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .setStyle(
//                androidx.media.app.NotificationCompat.MediaStyle()
//                .setMediaSession(mediaSessionCompat.sessionToken)
//                .setShowActionsInCompactView(0,1,2)
//                )
//            .addAction(R.drawable.previouss ,"Previous",null)
//            //.addAction(R.drawable.p,"Play",null )
//            .addAction(R.drawable.play_btn_selector,"Pause",PendingIntent.getBroadcast(context,0, Intent(context,
//                PlayerBroadcastReceiver::class.java).putExtra("action","pause"), PendingIntent.FLAG_ONE_SHOT))
//            .addAction(R.drawable.nextt ,"Next",null)
//            .setDeleteIntent(null)
//            .setShowWhen(false)
//            .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.notification_bg))
//
//    }

    @Provides
    @PlayerServiceScope
    fun provideNotificationChannel(): NotificationChannel{
       val channel = NotificationChannel(
            CHANNEL_ID,
            NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        channel.lockscreenVisibility = VISIBILITY_PUBLIC
        return channel
    }
    @Provides
    @PlayerServiceScope
    fun provideNotificationManager(context: Context): NotificationManagerCompat {
        return  NotificationManagerCompat.from(context)
    }



}