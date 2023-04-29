package com.example.musicapp.trending.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.favorites.presentation.TracksResultToSingleUiEventCommunicationMapper
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.PlayerCommunicationState
import com.example.musicapp.trending.domain.TrendingInteractor
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
class TrendingViewModel @Inject constructor(
    private val interactor: TrendingInteractor,
    private val trendingCommunication: TrendingCommunication,
    private val handleTrendingResult: HandleTrendingResult,
    private val dispatchersList: DispatchersList,
    private val playerCommunication: PlayerCommunication,
    private val favoriteTracksRepository: FavoriteTracksRepository,
    private val temporaryTracksCache: TemporaryTracksCache,
    private val mapper: TracksResultToSingleUiEventCommunicationMapper,
) : BaseViewModel(playerCommunication, temporaryTracksCache,dispatchersList), CollectTrendings {


    init {
        loadData()
    }

    fun loadData() = handleTrendingResult.handle(viewModelScope) {
        interactor.fetchData()
    }


    fun playMusic(item: MediaItem, position: Int) = viewModelScope.launch(dispatchersList.io()) {
        val queue = interactor.checkForNewQueue()
        if(queue.isNotEmpty()){
            val newQueue = mutableListOf<MediaItem>()
            newQueue.addAll(queue)
            withContext(dispatchersList.ui()) {
                playerCommunication.map(PlayerCommunicationState.SetQueue(newQueue,dispatchersList))
            }
        }
        withContext(dispatchersList.ui()) {
            playerCommunication.map(PlayerCommunicationState.Play(item, position))
        }
    }

    fun addTrackToFavorites(item: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
        favoriteTracksRepository.checkInsertData(item).map(mapper)
    }


    override suspend fun collectPlaylists(
        owner: LifecycleOwner,
        collector: FlowCollector<List<PlaylistUi>>,
    ) = trendingCommunication.collectPlaylists(owner, collector)

    override suspend fun collectState(
        owner: LifecycleOwner,
        collector: FlowCollector<TracksUiState>,
    ) = trendingCommunication.collectState(owner, collector)

    override suspend fun collectTracks(
        owner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>,
    ) = trendingCommunication.collectTracks(owner, collector)

}