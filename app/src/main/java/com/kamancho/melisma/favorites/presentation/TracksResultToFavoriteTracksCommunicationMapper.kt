package com.kamancho.melisma.favorites.presentation

import com.kamancho.melisma.app.core.TracksResultToTracksCommunicationMapper
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface TracksResultToFavoriteTracksCommunicationMapper:
    TracksResultToTracksCommunicationMapper<FavoritesUiState> {

    class Base @Inject constructor(communication: FavoritesTracksCommunication):
        TracksResultToFavoriteTracksCommunicationMapper,
        TracksResultToTracksCommunicationMapper.Abstract<FavoritesUiState>(communication) {


        override fun showError(message: String): FavoritesUiState = FavoritesUiState.Failure
        override fun showSuccess(): FavoritesUiState  = FavoritesUiState.Success
    }
}