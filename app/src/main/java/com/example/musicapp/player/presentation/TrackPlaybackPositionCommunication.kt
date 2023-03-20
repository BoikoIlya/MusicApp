package com.example.musicapp.player.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 20.03.2023.
 **/
interface TrackPlaybackPositionCommunication: Communication.Mutable<Pair<Int,String>> {

    class Base @Inject constructor(): TrackPlaybackPositionCommunication, Communication.UiUpdate<Pair<Int,String>>(
        Pair(0,"")
    )
}