package com.kamancho.melisma.favoritesplaylistdetails.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.favorites.presentation.HandleListFromCache
import javax.inject.Inject

/**
 * Created by HP on 05.08.2023.
 **/

interface HandleFavoritesPlaylistTracksFromCache: HandleListFromCache<MediaItem> {

    class Base @Inject constructor(
        playlistDetailsCommunication: PlaylistDetailsCommunication
    ) : HandleListFromCache.Abstract<MediaItem>(playlistDetailsCommunication),
        HandleFavoritesPlaylistTracksFromCache

}