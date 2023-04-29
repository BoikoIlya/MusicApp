package com.example.musicapp.trending.presentation


import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.Communication
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
interface TrendingCommunication: CollectTrendings {

    fun showUiState(state: TracksUiState)

    fun showTracks(tracks: List<MediaItem>)

    fun showPlayLists(playlists: List<PlaylistUi>)

    class Base @Inject constructor(
        private val trendingStateCommunication: TrendingStateCommunication,
        private val trendingPlaylistsCommunication: TrendingPlaylistsCommunication,
        private val trendingTracksCommunication: TrendingTracksCommunication
    ): TrendingCommunication{

        override fun showUiState(state: TracksUiState) = trendingStateCommunication.map(state)

        override fun showTracks(tracks: List<MediaItem>) = trendingTracksCommunication.map(tracks)

        override fun showPlayLists(playlists: List<PlaylistUi>) = trendingPlaylistsCommunication.map(playlists)

        override suspend fun collectState(
            owner: LifecycleOwner,
            collector: FlowCollector<TracksUiState>,
        ) = trendingStateCommunication.collect(owner, collector)

        override suspend fun collectPlaylists(
            owner: LifecycleOwner,
            collector: FlowCollector<List<PlaylistUi>>,
        ) = trendingPlaylistsCommunication.collect(owner,collector)

        override suspend fun collectTracks(
            owner: LifecycleOwner,
            collector: FlowCollector<List<MediaItem>>,
        ) = trendingTracksCommunication.collect(owner,collector)

    }

}

interface TrendingStateCommunication: Communication.Mutable<TracksUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<TracksUiState>(TracksUiState.Loading),
        TrendingStateCommunication
}

interface TrendingPlaylistsCommunication: Communication.Mutable<List<PlaylistUi>>{
    class Base @Inject constructor():
        Communication.UiUpdate<List<PlaylistUi>>(emptyList()),
        TrendingPlaylistsCommunication
}

interface TrendingTracksCommunication: Communication.Mutable<List<MediaItem>>{
    class Base @Inject constructor():
        Communication.UiUpdate<List<MediaItem>>(emptyList()),
        TrendingTracksCommunication
}

interface CollectTrendings{

    suspend fun collectState(owner: LifecycleOwner, collector: FlowCollector<TracksUiState>)

    suspend fun collectPlaylists(owner: LifecycleOwner, collector: FlowCollector<List<PlaylistUi>>)

    suspend fun collectTracks(owner: LifecycleOwner, collector: FlowCollector<List<MediaItem>>)
}
