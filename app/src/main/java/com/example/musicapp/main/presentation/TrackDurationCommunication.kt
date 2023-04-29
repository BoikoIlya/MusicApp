package com.example.musicapp.main.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
interface TrackDurationCommunication: Communication.Mutable<Long> {

    class Base @Inject constructor(): TrackDurationCommunication, Communication.UiUpdate<Long>(0)
}