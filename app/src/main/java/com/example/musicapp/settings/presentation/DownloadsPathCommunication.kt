package com.example.musicapp.settings.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.08.2023.
 **/
interface DownloadsPathCommunication: Communication.Mutable<String> {

    class Base @Inject constructor(): DownloadsPathCommunication, Communication.UiUpdate<String>("")
}