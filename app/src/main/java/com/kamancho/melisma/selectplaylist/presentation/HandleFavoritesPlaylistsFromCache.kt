package com.kamancho.melisma.selectplaylist.presentation

import com.kamancho.melisma.favorites.presentation.HandleListFromCache
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
interface HandleFavoritesPlaylistsFromCache: HandleListFromCache<PlaylistUi> {

    class Base @Inject constructor(
       selectPlaylistCommunication: SelectPlaylistCommunication
    ) : HandleListFromCache.Abstract<PlaylistUi>(selectPlaylistCommunication),
        HandleFavoritesPlaylistsFromCache

}