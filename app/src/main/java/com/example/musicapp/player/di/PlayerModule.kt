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
import com.example.musicapp.main.presentation.MainActivity
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