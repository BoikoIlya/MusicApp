package com.example.musicapp.selectplaylist.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.FavoritesTracksCommunication
import com.example.musicapp.favorites.presentation.HandleFavoritesListFromCache
import com.example.musicapp.favorites.presentation.HandleFavoritesTracksFromCache
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
interface HandleFavoritesPlaylistsFromCache: HandleFavoritesListFromCache<PlaylistUi> {

    class Base @Inject constructor(
       selectPlaylistCommunication: SelectPlaylistCommunication
    ) : HandleFavoritesListFromCache.Abstract<PlaylistUi>(selectPlaylistCommunication),
        HandleFavoritesPlaylistsFromCache

}