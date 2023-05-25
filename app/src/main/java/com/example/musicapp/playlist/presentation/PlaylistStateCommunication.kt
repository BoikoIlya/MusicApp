package com.example.musicapp.playlist.presentation

import com.example.musicapp.app.core.Communication
import com.example.musicapp.trending.presentation.TracksUiState
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface PlaylistStateCommunication: Communication.Mutable<TracksUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<TracksUiState>(TracksUiState.Loading),
        PlaylistStateCommunication
}