package com.example.musicapp.player.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 20.03.2023.
 **/
interface TrackPlaybackPositionCommunication: Communication.Mutable<Pair<Float,String>> {

    fun clearPosition()

    class Base @Inject constructor(): TrackPlaybackPositionCommunication, Communication.UiUpdate<Pair<Float,String>>(
        Pair(0f,"")
    ) {
        override fun clearPosition() {
            super.map(Pair(0f,"00:00"))
        }
    }
}