package com.example.musicapp.player.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicapp.main.di.App
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.PlayerCommunicationState
import javax.inject.Inject

/**
 * Created by HP on 26.06.2023.
 **/
class HeadPhonesReceiver: BroadcastReceiver() {

    @Inject
    lateinit var playerCommunication: PlayerCommunication

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent
            .playerComponent()
            .build()
            .inject(this)

            playerCommunication.map(PlayerCommunicationState.Pause)
    }

}