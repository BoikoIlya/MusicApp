package com.kamancho.melisma.selectplaylist.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.selectplaylist.domain.AddTrackToPlaylistInteractor
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.data.FetchPlaylistsRepository
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.BasePlaylistsViewModel
import com.kamancho.melisma.userplaylists.presentation.HandleFavoritesPlaylistsUiUpdate
import com.kamancho.melisma.userplaylists.presentation.HandlePlaylistsFetchingFromCache
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import com.kamancho.melisma.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
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