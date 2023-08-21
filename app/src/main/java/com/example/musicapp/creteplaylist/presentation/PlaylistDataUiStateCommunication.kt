package com.example.musicapp.creteplaylist.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/

interface PlaylistDataUiStateCommunication: Communication.Mutable<PlaylistDataUiState> {



    class Base @Inject constructor(): PlaylistDataUiStateCommunication,
        Communication.UiUpdate<PlaylistDataUiState>(PlaylistDataUiState.Success)
}