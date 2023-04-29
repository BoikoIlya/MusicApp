package com.example.musicapp.favorites.presentation


import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.UiEventState
import com.example.musicapp.main.presentation.UiEventsCommunication
import com.example.musicapp.musicdialog.presentation.MusicDialogFragment
import javax.inject.Inject


/**
 * @author Asatryan on 18.09.2022
 */
sealed interface TracksResult {

    interface Mapper<T> {
        suspend fun map(message: String, list: List<MediaItem>): T
    }

    suspend fun <T> map(mapper: Mapper<T>):T

    data class Success(
        private val list: List<MediaItem> = emptyList(),
        private val message: String = ""
    ) : TracksResult {
        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(message, list)
    }

    data class Failure(private val message: String) : TracksResult {

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(message, emptyList())
    }

    object Duplicate: TracksResult {

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("", emptyList())

    }

}

interface TracksResultToTracksCommunicationMapper: TracksResult.Mapper<Unit>{

    class Base @Inject constructor(
        private val communication: TracksCommunication
    ) : TracksResultToTracksCommunicationMapper{

        override suspend fun map(message: String, list: List<MediaItem>) {
            if(list.isNotEmpty()) {
                communication.showTracks(list)
                communication.showUiState(FavoriteTracksUiState.Success)
            }else communication.showUiState(FavoriteTracksUiState.Failure)
        }
    }
}

interface TracksResultToSingleUiEventCommunicationMapper: TracksResult.Mapper<Unit>{

    class Base @Inject constructor(
        private val singleUiEventCommunication: SingleUiEventCommunication,
        private val uiEventsCommunication: UiEventsCommunication
    ) : TracksResultToSingleUiEventCommunicationMapper{

        override suspend fun map(message: String, list: List<MediaItem>) {
            if(message.isNotEmpty())
                singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Success(message))
            else
                uiEventsCommunication.map(UiEventState.ShowDialog(MusicDialogFragment()))
        }
    }
}