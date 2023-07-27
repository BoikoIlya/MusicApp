package com.example.musicapp.selectplaylist.presentation

import com.example.musicapp.favorites.presentation.FavoritesTracksLoadingCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
interface SelectPlaylistCommunication: FavoritesUiCommunication<PlaylistUi> {

    class Base @Inject constructor(
        cachedPlaylistsCommunication: CachedPlaylistsCommunication,
        selectPlaylistPlaylistUiStateCommunication: SelectPlaylistUiStateCommunication,
        loadingCommunication: FavoritesTracksLoadingCommunication
    ): SelectPlaylistCommunication,FavoritesUiCommunication.Abstract<PlaylistUi>(
       selectPlaylistPlaylistUiStateCommunication, cachedPlaylistsCommunication,loadingCommunication
    )
}