package com.example.musicapp.favorites.presentation

import androidx.media3.common.MediaItem
import javax.inject.Inject

/**
 * Created by HP on 15.07.2023.
 **/

interface HandleFavoritesTracksFromCache: HandleFavoritesListFromCache<MediaItem> {

    class Base @Inject constructor(
        favoritesTracksCommunication: FavoritesTracksCommunication
    ) : HandleFavoritesListFromCache.Abstract<MediaItem>(favoritesTracksCommunication),
        HandleFavoritesTracksFromCache

}