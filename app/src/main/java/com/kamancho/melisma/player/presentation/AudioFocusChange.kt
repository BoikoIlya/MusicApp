package com.kamancho.melisma.player.presentation

import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Created by HP on 24.08.2023.
 **/
interface AudioFocusChange {

    fun requestAudioFocus()

    fun abandonAudioFocus()

    class BelowApi26(
        private val audioFocusChangeListener: AudioFocusChangeListener,
        private val audioManager: AudioManager
    ): AudioFocusChange {

        override fun requestAudioFocus() {
            audioManager.requestAudioFocus( audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }

        override fun abandonAudioFocus() {
            audioManager.abandonAudioFocus(audioFocusChangeListener)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    class AboveApi25(
        private val audioManager: AudioManager,
        private val audioFocusRequest: AudioFocusRequest
    ): AudioFocusChange {


        override fun requestAudioFocus() {
            audioManager.requestAudioFocus(audioFocusRequest)
        }

        override fun abandonAudioFocus() {
            audioManager.abandonAudioFocusRequest(audioFocusRequest)
        }
    }
}