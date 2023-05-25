package com.example.musicapp.playlist.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.TracksResultToTracksCommunicationMapper
import com.example.musicapp.trending.presentation.TracksUiState
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/

interface TracksResultToPlaylistTracksCommunicationMapper:
    TracksResultToTracksCommunicationMapper<TracksUiState> {

    class Base @Inject constructor(
        private val communication: PlaylistCommunication,
        ):
        TracksResultToPlaylistTracksCommunicationMapper,
        TracksResultToTracksCommunicationMapper.Abstract<TracksUiState>(communication) {

        override suspend fun map(
            message: String,
            list: List<MediaItem>,
            albumDescription: String,
            albumName: String,
            albumImgUrl: String
        ) {
            communication.showAdditionalPlaylistInfo(Triple(albumName,albumImgUrl,albumDescription))
            super.map(message,list)
        }

        override fun showError(message: String): TracksUiState = TracksUiState.Error(message)
        override fun showSuccess(): TracksUiState = TracksUiState.Success
    }
}