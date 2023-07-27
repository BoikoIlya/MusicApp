package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.favorites.presentation.TracksResult

/**
 * Created by HP on 23.05.2023.
 **/
interface TracksResultToTracksCommunicationMapper<T>: TracksResult.Mapper<Unit>{

    abstract class Abstract<T> (
        private val communication: UiCommunication<T,MediaItem>
    ) : TracksResultToTracksCommunicationMapper<T>{

        override suspend fun map(message: String, list: List<MediaItem>, error: Boolean, newId: Int) {
            if(list.isNotEmpty()) {
                communication.showData(list)
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


