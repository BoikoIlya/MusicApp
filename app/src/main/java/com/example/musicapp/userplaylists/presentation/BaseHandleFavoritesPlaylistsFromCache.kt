package com.example.musicapp.userplaylists.presentation

import com.example.musicapp.favorites.presentation.HandleListFromCache
import javax.inject.Inject

/**
 * Created by HP on 15.07.2023.
 **/
class BaseHandleFavoritesPlaylistsFromCache @Inject constructor(
    playlistsUiCommunication: FavoritesPlaylistsUiCommunication
): HandleListFromCache.Abstract<PlaylistUi>(playlistsUiCommunication),
HandleListFromCache<PlaylistUi>  {
}