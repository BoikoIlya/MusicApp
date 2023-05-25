package com.example.musicapp.player.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.musicapp.main.di.App
import javax.inject.Inject


@UnstableApi /**
 * Created by HP on 30.01.2023.
 **/


class PlayerService: MediaSessionService(){

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var mediaSession: MediaSession

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate() {
        super.onCreate()

        (this.applicationContext as App).appComponent
            .playerComponent()
            .build()
            .inject(this)

    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (!player.playWhenReady) {
             stopSelf()
        }
    }


    override fun onDestroy() {
        player.release()
        mediaSession.release()
        super.onDestroy()
    }
}