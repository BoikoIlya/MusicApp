package com.example.musicapp.playlist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.trending.presentation.TracksUiState
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface PlaylistCommunication: UiCommunication<TracksUiState, MediaItem>, CollectAdditionalPlaylistInfo {

    fun showAdditionalPlaylistInfo(data: Triple<String,String,String>)

    class Base @Inject constructor(
        playlistTracksCommunication: PlaylistTracksCommunication,
        playlistStateCommunication: PlaylistStateCommunication,
        private val additionalPlaylistInfo: AdditionalPlaylistInfoCommunication
    ): PlaylistCommunication, UiCommunication.Abstract<TracksUiState,MediaItem>(
        playlistStateCommunication,
        playlistTracksCommunication
    ) {
        override fun showAdditionalPlaylistInfo(data: Triple<String, String, String>) = additionalPlaylistInfo.map(data)

        override suspend fun collectAdditionalPlaylistInfo(
            owner: LifecycleOwner,
            collector: FlowCollector<Triple<String, String, String>>,
        ) = additionalPlaylistInfo.collect(owner,collector)
    }


}

interface CollectAdditionalPlaylistInfo{

    suspend fun collectAdditionalPlaylistInfo(
        owner: LifecycleOwner,
        collector: FlowCollector<Triple<String,String,String>>
    )
}

