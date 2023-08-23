package com.kamancho.melisma.frienddetails.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.FavoritesViewModel
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.frienddetails.data.FriendsDetailsCacheRepository
import com.kamancho.melisma.frienddetails.domain.FriendDetailsPlaylistsInteractor
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
class FriendPlaylistsViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val handleFriendsUpdate: HandleFriendsPlaylistsUpdate,
    private val cacheRepository:  FriendsDetailsCacheRepository,
    private val communication: FriendPlaylistsCommunication,
    private val handleFriendPlaylistsFromCache: HandleFriendPlaylistsFromCache,
    private val searchQueryFriendCommunication: SearchQueryFriendCommunication,
    private val interactor: FriendDetailsPlaylistsInteractor,
    private val mapper: PlaylistUi.Mapper<PlaylistUi>
): FavoritesViewModel<FavoritesUiState, PlaylistUi>,FriendsDataViewModel, ViewModel() {

    init {
        search("")
        update(false)
    }

    override fun update(loading: Boolean) {
        viewModelScope.launch(dispatchersList.io()){
            handleFriendsUpdate.handle(loading){ !cacheRepository.isFriendHavePlaylists() }
            search("")
        }
    }



    private val searchJob: Job?=null

   override fun search(query: String){
        searchJob?.cancel()

        viewModelScope.launch(dispatchersList.io()){
           val result =  cacheRepository.searchPlaylists(query)
            handleFriendPlaylistsFromCache.handle(result)
        }
    }

    fun savePlaylistData(item: PlaylistUi)  = interactor.savePlaylistData(item.map(mapper))



    override suspend fun collectSearchQuery(
        owner: LifecycleOwner,
        collector: FlowCollector<String>
    ) = searchQueryFriendCommunication.collect(owner, collector)

    override suspend fun collectLoading(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>
    ) = communication.collectLoading(owner, collector)

    override suspend fun collectState(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>,
    ) = communication.collectState(owner, collector)

    override suspend fun collectData(
        owner: LifecycleOwner,
        collector: FlowCollector<List<PlaylistUi>>,
    ) = communication.collectData(owner,collector)
}