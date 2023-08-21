package com.example.musicapp.frienddetails.presentation

import com.example.musicapp.favorites.presentation.HandleListFromCache
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistsUiCommunication
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 19.08.2023.
 **/
interface HandleFriendPlaylistsFromCache: HandleListFromCache<PlaylistUi> {

    class Base @Inject constructor(
        favoritesPlaylistsUiCommunication: FriendPlaylistsCommunication
    ) : HandleListFromCache.Abstract<PlaylistUi>(favoritesPlaylistsUiCommunication),
        HandleFriendPlaylistsFromCache

}