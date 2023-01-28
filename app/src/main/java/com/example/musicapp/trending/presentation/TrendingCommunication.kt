package com.example.musicapp.trending.presentation

import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.core.Communication
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
interface TrendingCommunication: CollectTrendings {

    fun showUiState(state: TrendingUiState)

    fun showTracks(tracks: List<TrackUi>)

    fun showPlayLists(playlists: List<PlaylistUi>)

    class Base @Inject constructor(
        private val trendingStateCommunication: TrendingStateCommunication,
        private val trendingPlaylistsCommunication: TrendingPlaylistsCommunication,
        private val trendingTracksCommunication: TrendingTracksCommunication
    ): TrendingCommunication{

        override fun showUiState(state: TrendingUiState) = trendingStateCommunication.map(state)

        override fun showTracks(tracks: List<TrackUi>) = trendingTracksCommunication.map(tracks)

        override fun showPlayLists(playlists: List<PlaylistUi>) = trendingPlaylistsCommunication.map(playlists)

        override suspend fun collectState(
            owner: LifecycleOwner,
            collector: FlowCollector<TrendingUiState>,
        ) = trendingStateCommunication.collect(owner, collector)

        override suspend fun collectPlaylists(
            owner: LifecycleOwner,
            collector: FlowCollector<List<PlaylistUi>>,
        ) = trendingPlaylistsCommunication.collect(owner,collector)

        override suspend fun collectTracks(
            owner: LifecycleOwner,
            collector: FlowCollector<List<TrackUi>>,
        ) = trendingTracksCommunication.collect(owner,collector)

    }

}

interface TrendingStateCommunication: Communication.Mutable<TrendingUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<TrendingUiState>(TrendingUiState.Success),
        TrendingStateCommunication
}

interface TrendingPlaylistsCommunication: Communication.Mutable<List<PlaylistUi>>{
    class Base @Inject constructor():
        Communication.UiUpdate<List<PlaylistUi>>(emptyList()),
        TrendingPlaylistsCommunication
}

interface TrendingTracksCommunication: Communication.Mutable<List<TrackUi>>{
    class Base @Inject constructor():
        Communication.UiUpdate<List<TrackUi>>(emptyList()),
        TrendingTracksCommunication
}

interface CollectTrendings{

    suspend fun collectState(owner: LifecycleOwner, collector: FlowCollector<TrendingUiState>)

    suspend fun collectPlaylists(owner: LifecycleOwner, collector: FlowCollector<List<PlaylistUi>>)

    suspend fun collectTracks(owner: LifecycleOwner, collector: FlowCollector<List<TrackUi>>)
}
