package com.kamancho.melisma.favoritesplaylistdetails.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.editplaylist.domain.PlaylistDetailsInteractor
import com.kamancho.melisma.favorites.presentation.HandleUpdate
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