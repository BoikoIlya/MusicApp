package com.example.musicapp.trending.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.TracksRepository
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.trending.domain.TrendingInteractor
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
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
    tracksRepository: TracksRepository,
) : BaseViewModel<TracksUiState>(
    playerCommunication,
    trendingCommunication,
    temporaryTracksCache,
    dispatchersList,
    tracksRepository,
    mapper
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