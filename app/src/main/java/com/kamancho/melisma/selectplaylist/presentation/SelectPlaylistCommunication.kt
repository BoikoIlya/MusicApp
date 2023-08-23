package com.kamancho.melisma.selectplaylist.presentation

import com.kamancho.melisma.favorites.presentation.FavoritesTracksLoadingCommunication
import com.kamancho.melisma.favorites.presentation.FavoritesUiCommunication
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
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