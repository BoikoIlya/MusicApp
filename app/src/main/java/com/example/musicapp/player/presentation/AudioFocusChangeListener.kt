package com.example.musicapp.player.presentation

import android.media.AudioManager
import android.util.Log
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.PlayerCommunicationState
import javax.inject.Inject

/**
 * Created by HP on 09.07.2023.
 **/
class AudioFocusChangeListener @Inject constructor(
    private val playerCommunication: PlayerCommunication
): AudioManager.OnAudioFocusChangeListener {

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> playerCommunication.map(PlayerCommunicationState.Pause)

            AudioManager.AUDIOFOCUS_LOSS -> playerCommunication.map(PlayerCommunicationState.Pause)

            AudioManager.AUDIOFOCUS_GAIN -> playerCommunication.map(PlayerCommunicationState.Resume)

        }
    }
}

