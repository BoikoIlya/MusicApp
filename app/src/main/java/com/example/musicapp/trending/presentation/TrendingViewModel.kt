package com.example.musicapp.trending.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.domain.QueryResult
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
) : ViewModel(), CollectTrendings {


    init {
        loadData()
    }

    fun loadData() = handleTrendingResult.handle(viewModelScope) {
        interactor.fetchData()
    }


    fun playMusic(item: MediaItem, position: Int) = viewModelScope.launch(dispatchersList.io()) {
        val query = interactor.checkForNewQuery()
        withContext(dispatchersList.ui()) {
            if (query.isNotEmpty()) playerCommunication.map(PlayerCommunicationState.SetQuery(query))
            playerCommunication.map(PlayerCommunicationState.Play(item, position))
        }
    }

    fun setQuery(tracks: List<MediaItem>) {
        playerCommunication.map(PlayerCommunicationState.SetQuery(tracks))
    }

    suspend fun collectSelectedTrackPosition(
        owner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    ) = playerCommunication.collectSelectedTrack(owner, collector)

    override suspend fun collectPlaylists(
        owner: LifecycleOwner,
        collector: FlowCollector<List<PlaylistUi>>,
    ) = trendingCommunication.collectPlaylists(owner, collector)

    override suspend fun collectState(
        owner: LifecycleOwner,
        collector: FlowCollector<TrendingUiState>,
    ) = trendingCommunication.collectState(owner, collector)

    override suspend fun collectTracks(
        owner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>,
    ) = trendingCommunication.collectTracks(owner, collector)

}