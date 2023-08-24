package com.kamancho.melisma.player.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 05.07.2023.
 **/
interface PlayingTrackIdCommunication: Communication.Mutable<String> {

    class Base @Inject constructor(): PlayingTrackIdCommunication, Communication.UiUpdate<String>("0")
}