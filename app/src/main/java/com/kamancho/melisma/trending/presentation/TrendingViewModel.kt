package com.kamancho.melisma.trending.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.BaseViewModel
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.trending.domain.TrendingInteractor
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
class TrendingViewModel @Inject constructor(
    private val interactor: TrendingInteractor,
    private val trendingCommunication: TrendingCommunication,
    private val handleTrendingResult: HandleTrendingResult,
    dispatchersList: DispatchersList,
    playerCommunication: PlayerCommunication,
    temporaryTracksCache: TemporaryTracksCache,
    mapper: TracksResultToUiEventCommunicationMapper,
    tracksInteractor: Interactor<MediaItem,TracksResult>,
    trackChecker: TrackChecker
) : BaseViewModel<TrendingUiState>(
    playerCommunication,
    trendingCommunication,
    temporaryTracksCache,
    dispatchersList,
    tracksInteractor,
    mapper,
    trackChecker
), CollectTrendings {


    init {
        loadData()
    }

    fun loadData() = handleTrendingResult.handle(viewModelScope) {
        interactor.fetchData()
    }

    override suspend fun collectPlaylists(
        owner: LifecycleOwner,
        collector: FlowCollector<List<TrendingTopBarItemUi>>,
    ) = trendingCommunication.collectPlaylists(owner, collector)

     suspend fun collectEmbeddedPlaylists(
        owner: LifecycleOwner,
        collector: FlowCollector<List<TrendingTopBarItemUi>>,
    ) = trendingCommunication.collectEmbeddedPlaylists(owner, collector)


}