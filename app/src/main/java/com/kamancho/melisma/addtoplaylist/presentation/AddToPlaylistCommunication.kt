package com.kamancho.melisma.addtoplaylist.presentation

import com.kamancho.melisma.favorites.presentation.FavoritesTracksLoadingCommunication
import com.kamancho.melisma.favorites.presentation.FavoritesUiCommunication
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


