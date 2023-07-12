package com.example.musicapp.favorites.presentation

import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/

interface FavoritesCommunication: UiCommunication<FavoritesUiState> {

    class Base @Inject constructor(
        uiStateCommunication: FavoritesStateCommunication,
        favoritesTracksCommunication: FavoritesTrackListCommunication
    ) : UiCommunication.Abstract<FavoritesUiState>(
        uiStateCommunication,
        favoritesTracksCommunication
    ), FavoritesCommunication {

    }

}