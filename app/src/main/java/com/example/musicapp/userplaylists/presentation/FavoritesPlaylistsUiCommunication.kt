package com.example.musicapp.userplaylists.presentation

import com.example.musicapp.favorites.presentation.FavoritesTracksLoadingCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistLoadingCommunication
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/

interface FavoritesPlaylistsUiCommunication: FavoritesUiCommunication<PlaylistUi> {

    class Base @Inject constructor(
        uiStateCommunication: PlaylistsUiStateCommunication,
        favoritesPlaylistCommunication: FavoritesPlaylistCommunication,
        playlistLoadingCommunication: FavoritesTracksLoadingCommunication
    ) : FavoritesUiCommunication.Abstract<PlaylistUi>(
        uiStateCommunication,
        favoritesPlaylistCommunication,
        playlistLoadingCommunication
    ), FavoritesPlaylistsUiCommunication

}
