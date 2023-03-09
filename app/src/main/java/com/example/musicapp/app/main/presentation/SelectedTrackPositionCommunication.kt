package com.example.musicapp.app.main.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by HP on 31.01.2023.
 **/
interface SelectedTrackPositionCommunication: Communication.Mutable<Int> {


    @Singleton
    class Base @Inject constructor(): Communication.UiUpdate<Int>(-1),
        SelectedTrackPositionCommunication
}