package com.example.musicapp.userplaylists.presentation

import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.favorites.presentation.HandleUpdate
import com.example.musicapp.selectplaylist.presentation.SelectPlaylistCommunication
import com.example.musicapp.userplaylists.domain.FavoritesPlaylistsInteractor
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