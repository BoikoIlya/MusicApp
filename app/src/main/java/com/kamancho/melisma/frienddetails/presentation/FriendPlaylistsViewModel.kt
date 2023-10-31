package com.kamancho.melisma.frienddetails.presentation

import android.util.Log
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

    override fun update(id: String, loading: Boolean,shouldUpdate: Boolean) {
        if(!shouldUpdate) return
        search("",id)
        viewModelScope.launch(dispatchersList.io()){
            handleFriendsUpdate.handle(loading,id){ !cacheRepository.isFriendHavePlaylists(id) }
            search("",id)
        }
    }






    private val searchJob: Job?=null

   override fun search(query: String,id: String){
        searchJob?.cancel()

        viewModelScope.launch(dispatchersList.io()){
           val result =  cacheRepository.searchPlaylists(query,id)
            handleFriendPlaylistsFromCache.handle(result)
        }
    }





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