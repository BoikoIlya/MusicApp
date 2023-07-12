package com.example.musicapp.trending.presentation


import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.favorites.presentation.UiCommunication
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
interface TrendingCommunication: CollectTrendings, UiCommunication<TracksUiState> {

    fun showPlayLists(playlists: List<PlaylistUi>)

    class Base @Inject constructor(
        private val trendingPlaylistsCommunication: TrendingPlaylistsCommunication,
        trendingStateCommunication: TrendingStateCommunication,
        trendingTracksCommunication: TrendingTracksCommunication
    ): TrendingCommunication,
        UiCommunication.Abstract<TracksUiState>(
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



