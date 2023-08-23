package com.kamancho.melisma.trending.presentation


import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.trending.domain.TopBarItemDomain
import com.kamancho.melisma.trending.domain.TrackDomain
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

