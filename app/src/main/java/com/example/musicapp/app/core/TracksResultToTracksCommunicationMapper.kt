package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.FavoriteTracksUiState
import com.example.musicapp.favorites.presentation.FavoritesCommunication
import com.example.musicapp.favorites.presentation.TracksCommunication
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.playlist.presentation.PlaylistCommunication
import com.example.musicapp.trending.presentation.TracksUiState
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface TracksResultToTracksCommunicationMapper<T>: TracksResult.Mapper<Unit>{

    abstract class Abstract<T> (
        private val communication: TracksCommunication<T>
    ) : TracksResultToTracksCommunicationMapper<T>{

        override suspend fun map(message: String, list: List<MediaItem>) {
            if(list.isNotEmpty()) {
                communication.showTracks(list)
                communication.showUiState(showSuccess())
            }else communication.showUiState(showError(message))
        }


        protected abstract fun showError(message: String): T
        protected abstract fun showSuccess(): T

        override suspend fun map(
            message: String,
            list: List<MediaItem>,
            albumDescription: String,
            albumName: String,
            albumImgUrl: String
        ) = Unit
    }
}


