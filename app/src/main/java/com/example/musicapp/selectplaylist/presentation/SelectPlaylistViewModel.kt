package com.example.musicapp.selectplaylist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.selectplaylist.domain.AddTrackToPlaylistInteractor
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.userplaylists.data.FetchPlaylistsRepository
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.BasePlaylistsViewModel
import com.example.musicapp.userplaylists.presentation.HandleFavoritesPlaylistsUiUpdate
import com.example.musicapp.userplaylists.presentation.HandlePlaylistsFetchingFromCache
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.example.musicapp.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
class SelectPlaylistViewModel @Inject constructor(
    private val interactor: AddTrackToPlaylistInteractor,
    private val mapper: PlaylistsResultUpdateToUiEventMapper,
    private val dispatchersList: DispatchersList,
    private val transfer: DataTransfer<TrackDomain>,
    private val toTrackIdMapper: TrackDomain.Mapper<Int>,
    private val toPlaylistIdMapper: PlaylistUi.Mapper<String>,
    private val toPlaylistUiMapper: PlaylistDomain.Mapper<PlaylistUi>,
    private val communication: SelectPlaylistCommunication,
    handlerFavoritesUiUpdate: HandleFavoritesPlaylistsUiUpdate,
    private val handlePlaylistsFetchingFromCache: HandlePlaylistsFetchingFromCache,
    private val repository: FetchPlaylistsRepository,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val handleFavoritesListFromCachePlaylistUi: HandleFavoritesPlaylistsFromCache
): BasePlaylistsViewModel(
    handlerFavoritesUiUpdate,
    dispatchersList,
    communication,
    repository,

) {


    fun fetch(query: String){
        handlePlaylistsFetchingFromCache.fetch(viewModelScope){
            repository.fetchUserCreated(query).collect{list->
                handleFavoritesListFromCachePlaylistUi.handle(list.map { it.map(toPlaylistUiMapper) })
            }
        }
    }

    fun addToPlaylist(playlistUi: PlaylistUi) = viewModelScope.launch(dispatchersList.io()) {
        interactor.addTrackToPlaylist(playlistUi.map(toPlaylistIdMapper).toInt(),transfer.read()!!.map(toTrackIdMapper))
            .map(mapper)
    }

    suspend fun collectGlobalSingleUiEventCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<SingleUiEventState>,
    ) = globalSingleUiEventCommunication.collect(owner,collector)
}