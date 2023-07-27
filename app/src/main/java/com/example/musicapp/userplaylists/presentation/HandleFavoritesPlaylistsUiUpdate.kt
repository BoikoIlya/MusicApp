package com.example.musicapp.userplaylists.presentation

import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.favorites.presentation.HandlerFavoritesUiUpdate
import com.example.musicapp.selectplaylist.presentation.SelectPlaylistCommunication
import com.example.musicapp.userplaylists.domain.FavoritesPlaylistsInteractor
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/

interface HandleFavoritesPlaylistsUiUpdate: HandlerFavoritesUiUpdate {


    class BaseForSelectPlaylist @Inject constructor(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        selectPlaylistCommunication: SelectPlaylistCommunication,
        interactor: FavoritesPlaylistsInteractor
    ): HandleFavoritesPlaylistsUiUpdate,
        HandlerFavoritesUiUpdate.Abstract<PlaylistUi, PlaylistsResult>(
            selectPlaylistCommunication,
            globalSingleUiEventCommunication,
            interactor
        )

    class BaseForFavoritesPlaylists @Inject constructor(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        favoritesTracksCommunication: FavoritesPlaylistsUiCommunication,
        interactor: FavoritesPlaylistsInteractor
    ): HandleFavoritesPlaylistsUiUpdate,
        HandlerFavoritesUiUpdate.Abstract<PlaylistUi, PlaylistsResult>(
        favoritesTracksCommunication,
        globalSingleUiEventCommunication,
        interactor
    )
}