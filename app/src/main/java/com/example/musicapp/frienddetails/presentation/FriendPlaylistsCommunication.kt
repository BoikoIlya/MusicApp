package com.example.musicapp.frienddetails.presentation

import com.example.musicapp.favorites.presentation.FavoritesUiCommunication
import com.example.musicapp.favoritesplaylistdetails.presentation.PlaylistLoadingCommunication
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistCommunication
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistsUiCommunication
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.example.musicapp.userplaylists.presentation.PlaylistsUiStateCommunication
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