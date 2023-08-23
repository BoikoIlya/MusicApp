package com.kamancho.melisma.frienddetails.presentation

import com.kamancho.melisma.favorites.presentation.HandleListFromCache
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
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