package com.example.musicapp.playlist.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.Communication
import com.example.musicapp.trending.presentation.TracksUiState
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface PlaylistTracksCommunication: Communication.Mutable<List<MediaItem>>{
    class Base @Inject constructor():
        Communication.UiUpdate<List<MediaItem>>(emptyList()),
        PlaylistTracksCommunication
}