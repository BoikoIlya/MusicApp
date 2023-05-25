package com.example.musicapp.favorites.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.TracksResultToTracksCommunicationMapper
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface TracksResultToFavoriteTracksCommunicationMapper:
    TracksResultToTracksCommunicationMapper<FavoriteTracksUiState> {

    class Base @Inject constructor(communication: FavoritesCommunication):
        TracksResultToFavoriteTracksCommunicationMapper,
        TracksResultToTracksCommunicationMapper.Abstract<FavoriteTracksUiState>(communication) {


        override fun showError(message: String): FavoriteTracksUiState = FavoriteTracksUiState.Failure
        override fun showSuccess(): FavoriteTracksUiState  = FavoriteTracksUiState.Success
    }
}