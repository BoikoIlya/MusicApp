package com.example.musicapp.favorites.presentation

import android.util.Log
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/

interface FavoritesCommunication: TracksCommunication<FavoriteTracksUiState> {

    class Base @Inject constructor(
        uiStateCommunication: FavoritesStateCommunication,
        favoritesTracksCommunication: FavoritesTrackListCommunication
    ) : TracksCommunication.Abstract<FavoriteTracksUiState>(
        uiStateCommunication,
        favoritesTracksCommunication
    ), FavoritesCommunication {

//        override fun showUiState(state: FavoriteTracksUiState) {
//            Log.d("tag", "showUiState: $state")
//        }
    }

}