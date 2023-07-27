package com.example.musicapp.trending.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.trending.domain.TrendingInteractor
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
) : BaseViewModel<TracksUiState>(
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

    fun savePlaylistId(id: String) = interactor.savePlaylistId(id)

    override suspend fun collectPlaylists(
        owner: LifecycleOwner,
        collector: FlowCollector<List<PlaylistUi>>,
    ) = trendingCommunication.collectPlaylists(owner, collector)


}