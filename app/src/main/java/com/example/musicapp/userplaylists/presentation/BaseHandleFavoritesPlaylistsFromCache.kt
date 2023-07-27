package com.example.musicapp.userplaylists.presentation

import com.example.musicapp.favorites.presentation.HandleFavoritesListFromCache
import javax.inject.Inject

/**
 * Created by HP on 15.07.2023.
 **/
class BaseHandleFavoritesPlaylistsFromCache @Inject constructor(
    playlistsUiCommunication: FavoritesPlaylistsUiCommunication
): HandleFavoritesListFromCache.Abstract<PlaylistUi>(playlistsUiCommunication),
HandleFavoritesListFromCache<PlaylistUi>  {
}