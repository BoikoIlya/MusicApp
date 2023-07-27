package com.example.musicapp.userplaylists.presentation

import android.view.View
import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 20.07.2023.
 **/
interface CanEditPlaylistStateCommunication: Communication.Mutable<Int> {

    class  Base @Inject constructor(): CanEditPlaylistStateCommunication,
        Communication.UiUpdate<Int>(View.GONE)
}