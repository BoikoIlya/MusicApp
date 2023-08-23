package com.kamancho.melisma.frienddetails.presentation

import com.kamancho.melisma.favorites.presentation.FavoritesUiCommunication
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 19.08.2023.
 **/

interface FriendPlaylistsCommunication: FavoritesUiCommunication<PlaylistUi> {

    class Base @Inject constructor(
        uiStateCommunication: FriendPlaylistsUiStateCommunication,
        favoritesPlaylistCommunication: FriendPlaylistsListCommunication,
        playlistLoadingCommunication: FriendPlaylistsLoadingCommunication
    ) : FavoritesUiCommunication.Abstract<PlaylistUi>(
        uiStateCommunication,
        favoritesPlaylistCommunication,
        playlistLoadingCommunication
    ), FriendPlaylistsCommunication

}