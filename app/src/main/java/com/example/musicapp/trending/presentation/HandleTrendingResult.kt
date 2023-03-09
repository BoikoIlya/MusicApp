package com.example.musicapp.trending.presentation


import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.trending.domain.PlaylistDomain
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
    private val playlistsMapper: PlaylistDomain.Mapper<PlaylistUi>,
    private val tracksMapper: TrackDomain.Mapper<MediaItem>
): TrendingResult.Mapper<Unit>{

    override fun map(data: Pair<List<PlaylistDomain>, List<TrackDomain>>, message: String) {
        communication.showUiState(
        if(data.first.isEmpty() || data.second.isEmpty()) TrendingUiState.Error(message)
        else {
            communication.showPlayLists(data.first.map { it.map(playlistsMapper) })
            communication.showTracks(data.second.map { it.map(tracksMapper) })
            TrendingUiState.Success
        }
        )
    }

}

