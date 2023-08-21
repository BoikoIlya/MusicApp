package com.example.musicapp.addtoplaylist.presentation

import com.example.musicapp.favorites.presentation.HandleListFromCache
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