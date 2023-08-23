package com.kamancho.melisma.favorites.presentation

import androidx.media3.common.MediaItem
import javax.inject.Inject

/**
 * Created by HP on 15.07.2023.
 **/

interface HandleFavoritesTracksFromCache: HandleListFromCache<MediaItem> {

    class Base @Inject constructor(
        favoritesTracksCommunication: FavoritesTracksCommunication
    ) : HandleListFromCache.Abstract<MediaItem>(favoritesTracksCommunication),
        HandleFavoritesTracksFromCache

}