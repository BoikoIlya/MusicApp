package com.example.musicapp.player.di

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent

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
        const val ACTION_SONG_FRAG = "action_player_fragment"
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
            addNextIntent(Intent(context , MainActivity::class.java).putExtra(ACTION_SONG_FRAG,true))
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


}