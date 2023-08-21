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
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/
interface HandleTrendingResult {

    fun handle(
        coroutineScope: CoroutineScope,
        fetchData: suspend ()-> TrendingResult,
    )

    class Base @Inject constructor(
        private val communication: TrendingCommunication,
        private val dispatchersList: DispatchersList,
        private val mapper: TrendingResult.Mapper<Unit>,
    ): HandleTrendingResult{

        override fun handle(
            coroutineScope: CoroutineScope,
            fetchData: suspend () -> TrendingResult,
        ) {
            communication.showUiState(TrendingUiState.Loading)
            coroutineScope.launch(dispatchersList.io()) {
                val result = fetchData.invoke()
                 result.map(mapper)
            }

        }
    }
}

class TrendingResultMapper @Inject constructor(
    private val communication: TrendingCommunication,
    private val playlistsMapper: TopBarItemDomain.Mapper<TrendingTopBarItemUi>,
    private val tracksMapper: TrackDomain.Mapper<MediaItem>,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val temporaryTracksCache: TemporaryTracksCache
): TrendingResult.Mapper<Unit>{

    override suspend fun map(data: Pair<List<TopBarItemDomain>, List<TrackDomain>>, message: String) {
        communication.showUiState(TrendingUiState.DisableLoading)
        if(data.second.isEmpty()) {
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
        } else {
            communication.showData(data.second.map { it.map(tracksMapper) })
        }
        Log.d("tag", "map: ${data.first.size} ${data.second.size} ")
        communication.showPlayLists(data.first.map { it.map(playlistsMapper) })
    }

}

