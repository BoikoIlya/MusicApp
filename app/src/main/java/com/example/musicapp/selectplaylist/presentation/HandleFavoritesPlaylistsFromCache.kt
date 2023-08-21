package com.example.musicapp.selectplaylist.presentation

import com.example.musicapp.favorites.presentation.HandleListFromCache
import com.example.musicapp.userplaylists.presentation.PlaylistUi
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