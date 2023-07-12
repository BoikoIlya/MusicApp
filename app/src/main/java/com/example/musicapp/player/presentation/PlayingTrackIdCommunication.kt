package com.example.musicapp.player.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 05.07.2023.
 **/
interface PlayingTrackIdCommunication: Communication.Mutable<Int> {

    class Base @Inject constructor(): PlayingTrackIdCommunication, Communication.UiUpdate<Int>(0)
}