package com.kamancho.melisma.trending.presentation


import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.kamancho.melisma.favorites.presentation.UiCommunication
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
interface TrendingCommunication: CollectTrendings, UiCommunication<TrendingUiState, MediaItem> {

    fun showPlayLists(playlists: List<TrendingTopBarItemUi>)

    fun showBottomPagingState(state: TrendingBottomPagingState)

    suspend fun collectBottomPagingState(
        owner: LifecycleOwner,
        collector: FlowCollector<TrendingBottomPagingState>,
    )

    class Base @Inject constructor(
        private val trendingPlaylistsCommunication: TrendingPlaylistsCommunication,
        private val trendingBottomPaggingStateCommunication: TrendingBottomPaggingStateCommunication,
        trendingStateCommunication: TrendingStateCommunication,
        trendingTracksCommunication: TrendingTracksCommunication
    ): TrendingCommunication,
        UiCommunication.Abstract<TrendingUiState,MediaItem>(
            trendingStateCommunication,trendingTracksCommunication){

        override fun showPlayLists(playlists: List<TrendingTopBarItemUi>) = trendingPlaylistsCommunication.map(playlists)

        override fun showBottomPagingState(state: TrendingBottomPagingState) = trendingBottomPaggingStateCommunication.map(state)

        override suspend fun collectBottomPagingState(
            owner: LifecycleOwner,
            collector: FlowCollector<TrendingBottomPagingState>,
        ) = trendingBottomPaggingStateCommunication.collect(owner, collector)

        override suspend fun collectPlaylists(
            owner: LifecycleOwner,
            collector: FlowCollector<List<TrendingTopBarItemUi>>,
        ) = trendingPlaylistsCommunication.collect(owner,collector)



    }

}



interface CollectTrendings{



    suspend fun collectPlaylists(owner: LifecycleOwner, collector: FlowCollector<List<TrendingTopBarItemUi>>)

}

interface CollectUiState<T> {
    suspend fun collectState(owner: LifecycleOwner, collector: FlowCollector<T>)
}



