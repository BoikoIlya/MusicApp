package com.example.musicapp.player.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.musicapp.main.di.App
import com.example.musicapp.main.presentation.PlayerCommunication
import javax.inject.Inject


@UnstableApi /**
 * Created by HP on 30.01.2023.
 **/


class PlayerService: MediaSessionService(){

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var audioFocusRequest: AudioFocusRequest

    private lateinit var receiver: HeadPhonesReceiver

    private lateinit var audioManager: AudioManager



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

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.requestAudioFocus(audioFocusRequest)

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
        audioManager.abandonAudioFocusRequest(audioFocusRequest)
        super.onDestroy()
    }
}