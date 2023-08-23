package com.kamancho.melisma.userplaylists.presentation

import com.kamancho.melisma.favorites.presentation.FavoritesTracksLoadingCommunication
import com.kamancho.melisma.favorites.presentation.FavoritesUiCommunication
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
