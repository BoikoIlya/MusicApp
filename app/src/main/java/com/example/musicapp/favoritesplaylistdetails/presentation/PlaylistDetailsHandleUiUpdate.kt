package com.example.musicapp.favoritesplaylistdetails.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.editplaylist.domain.PlaylistDetailsInteractor
import com.example.musicapp.favorites.presentation.HandleUpdate
import javax.inject.Inject

/**
 * Created by HP on 02.08.2023.
 **/
interface PlaylistDetailsHandleUiUpdate: HandleUpdate {

    class Base @Inject constructor(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        interctor: PlaylistDetailsInteractor,
        playlistDetailsCommunication: PlaylistDetailsCommunication
    ): HandleUpdate.Abstract<MediaItem>(
        playlistDetailsCommunication,globalSingleUiEventCommunication,interctor
    ),PlaylistDetailsHandleUiUpdate
}