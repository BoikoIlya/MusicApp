package com.kamancho.melisma.player.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Process
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.kamancho.melisma.main.di.App
import javax.inject.Inject


/**
 * Created by HP on 30.01.2023.
 **/


class PlayerService: MediaSessionService(){

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var mediaSession: MediaSession

    private lateinit var receiver: HeadPhonesReceiver


    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate() {
        super.onCreate()

        (this.applicationContext as App).appComponent
            .playerComponent()
            .build()
            .inject(this)


        receiver = HeadPhonesReceiver()
        val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        registerReceiver(receiver, intentFilter)



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
        unregisterReceiver(receiver)
        super.onDestroy()
    }


}