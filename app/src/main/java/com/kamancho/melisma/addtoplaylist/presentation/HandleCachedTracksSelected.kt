package com.kamancho.melisma.addtoplaylist.presentation

import com.kamancho.melisma.favorites.presentation.HandleListFromCache
import javax.inject.Inject

/**
 * Created by HP on 28.07.2023.
 **/
interface HandleCachedTracksSelected: HandleListFromCache<SelectedTrackUi> {

    class Base @Inject constructor(
        addToPlaylistCommunication: AddToPlaylistCommunication
    ) : HandleListFromCache.Abstract<SelectedTrackUi>(addToPlaylistCommunication),
        HandleCachedTracksSelected

}