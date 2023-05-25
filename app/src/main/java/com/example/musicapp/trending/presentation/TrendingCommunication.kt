package com.example.musicapp.trending.presentation


import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.CollectTracksAndUiState
import com.example.musicapp.app.core.Communication
import com.example.musicapp.favorites.presentation.CollectTracks
import com.example.musicapp.favorites.presentation.TracksCommunication
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
interface TrendingCommunication: CollectTrendings, TracksCommunication<TracksUiState> {

    fun showPlayLists(playlists: List<PlaylistUi>)

    class Base @Inject constructor(
        private val trendingPlaylistsCommunication: TrendingPlaylistsCommunication,
        trendingStateCommunication: TrendingStateCommunication,
        trendingTracksCommunication: TrendingTracksCommunication
    ): TrendingCommunication,
        TracksCommunication.Abstract<TracksUiState>(
            trendingStateCommunication,trendingTracksCommunication){

        override fun showPlayLists(playlists: List<PlaylistUi>) = trendingPlaylistsCommunication.map(playlists)

        override suspend fun collectPlaylists(
            owner: LifecycleOwner,
            collector: FlowCollector<List<PlaylistUi>>,
        ) = trendingPlaylistsCommunication.collect(owner,collector)



    }

}



interface CollectTrendings{



    suspend fun collectPlaylists(owner: LifecycleOwner, collector: FlowCollector<List<PlaylistUi>>)

}

interface CollectUiState<T> {
    suspend fun collectState(owner: LifecycleOwner, collector: FlowCollector<T>) //TracksUiState
}



