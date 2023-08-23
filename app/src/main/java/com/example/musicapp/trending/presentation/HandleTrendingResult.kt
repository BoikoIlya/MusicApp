package com.example.musicapp.trending.presentation


import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.trending.domain.TopBarItemDomain
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/
interface HandleTrendingResult {

    fun handle(
        coroutineScope: CoroutineScope,
        fetchData: suspend ()-> Flow<TrendingResult>,
    )

    class Base @Inject constructor(
        private val communication: TrendingCommunication,
        private val dispatchersList: DispatchersList,
        private val mapper: TrendingResult.Mapper<Unit>,
    ): HandleTrendingResult{

        override fun handle(
            coroutineScope: CoroutineScope,
            fetchData: suspend () -> Flow<TrendingResult>,
        ) {
            communication.showUiState(TrendingUiState.Loading)
            coroutineScope.launch(dispatchersList.io()) {
                 fetchData.invoke().collectLatest {result->
                     result.map(mapper)
                 }
            }

        }
    }
}

class TrendingResultMapper @Inject constructor(
    private val communication: TrendingCommunication,
    private val playlistsMapper: TopBarItemDomain.Mapper<TrendingTopBarItemUi>,
    private val tracksMapper: TrackDomain.Mapper<MediaItem>,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
): TrendingResult.Mapper<Unit>{

    override suspend fun map(data: Pair<List<TopBarItemDomain>, List<TrackDomain>>, message: String) {
        communication.showPlayLists(data.first.map { it.map(playlistsMapper) })
        if(data.second.isNotEmpty()) {
            communication.showData(data.second.map { it.map(tracksMapper) })
            communication.showUiState(TrendingUiState.DisableLoading)
        }
        if(message.isNotEmpty()) {
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
            communication.showUiState(TrendingUiState.DisableLoading)
        }
    }

}

