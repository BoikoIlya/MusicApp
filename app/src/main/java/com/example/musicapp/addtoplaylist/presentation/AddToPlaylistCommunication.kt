package com.example.musicapp.addtoplaylist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.creteplaylist.presentation.SelectedTracksCommunication
import com.example.musicapp.favorites.presentation.FavoritesTracksLoadingCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.presentation.UiCommunication
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface AddToPlaylistCommunication: FavoritesUiCommunication<SelectedTrackUi> {


    class Base @Inject constructor(
        addToPlaylistUiStateCommunication: AddToPlaylistUiStateCommunication,
        cachedTracksCommunication: CachedTracksCommunication,
        loadingCommunication: FavoritesTracksLoadingCommunication,
    ): AddToPlaylistCommunication,
        FavoritesUiCommunication.Abstract<SelectedTrackUi>(
            addToPlaylistUiStateCommunication,
            cachedTracksCommunication,
            loadingCommunication
        )

}


