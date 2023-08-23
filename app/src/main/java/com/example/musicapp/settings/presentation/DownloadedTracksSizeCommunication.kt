package com.example.musicapp.settings.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.08.2023.
 **/
interface DownloadedTracksSizeCommunication: Communication.Mutable<String> {

    class Base @Inject constructor(): DownloadedTracksSizeCommunication, Communication.UiUpdate<String>("")
}