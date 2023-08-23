package com.kamancho.melisma.userplaylists.presentation

import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.favorites.presentation.HandleUpdate
import com.kamancho.melisma.selectplaylist.presentation.SelectPlaylistCommunication
import com.kamancho.melisma.userplaylists.domain.FavoritesPlaylistsInteractor
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/

interface HandleFavoritesPlaylistsUiUpdate: HandleUpdate {


    class BaseForSelectPlaylist @Inject constructor(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        selectPlaylistCommunication: SelectPlaylistCommunication,
        interactor: FavoritesPlaylistsInteractor
    ): HandleFavoritesPlaylistsUiUpdate,
        HandleUpdate.Abstract<PlaylistUi>(
            selectPlaylistCommunication,
            globalSingleUiEventCommunication,
            interactor
        )

    class BaseForFavoritesPlaylists @Inject constructor(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        favoritesTracksCommunication: FavoritesPlaylistsUiCommunication,
        interactor: FavoritesPlaylistsInteractor
    ): HandleFavoritesPlaylistsUiUpdate,
        HandleUpdate.Abstract<PlaylistUi>(
        favoritesTracksCommunication,
        globalSingleUiEventCommunication,
        interactor
    )
}