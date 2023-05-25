package com.example.musicapp.playlist.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface AdditionalPlaylistInfoCommunication: Communication.Mutable<Triple<String,String,String>> {

    class Base @Inject constructor(): AdditionalPlaylistInfoCommunication,Communication.UiUpdate<Triple<String,String,String>>(
        Triple("","","")
    )
}