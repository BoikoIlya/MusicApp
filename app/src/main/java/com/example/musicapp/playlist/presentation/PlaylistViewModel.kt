package com.example.musicapp.playlist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.playlist.domain.PlaylistInteractor
import com.example.musicapp.trending.presentation.TracksUiState
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 22.05.2023.
 **/
class PlaylistViewModel @Inject constructor(
    private val playlistInteractor: PlaylistInteractor,
    private val dispatchersList: DispatchersList,
    private val dataMapper: TracksResultToPlaylistTracksCommunicationMapper,
    private val additionalPlaylistInfo: AdditionalPlaylistInfoCommunication,
    private val playlistCommunication: PlaylistCommunication,
    playerCommunication: PlayerCommunication,
    temporaryTracksCache: TemporaryTracksCache,
    tracksRepository: FavoriteTracksRepository,
    mapper: TracksResultToUiEventCommunicationMapper
): BaseViewModel<TracksUiState>(
    playerCommunication,
    playlistCommunication,
    temporaryTracksCache,
    dispatchersList,
    tracksRepository,
    mapper
), CollectAdditionalPlaylistInfo {

    init {
        loadData()
    }

    fun loadData() = viewModelScope.launch(dispatchersList.io()) {
        playlistCommunication.showUiState(TracksUiState.Loading)
        playlistInteractor.fetchData().map(dataMapper)
    }

    override suspend fun collectAdditionalPlaylistInfo(
        owner: LifecycleOwner,
        collector: FlowCollector<Triple<String, String, String>>,
    )= additionalPlaylistInfo.collect(owner,collector)

}