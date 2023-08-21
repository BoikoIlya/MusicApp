package com.example.musicapp.frienddetails.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.CacheRepository
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.FavoritesViewModel
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.presentation.FavoritesTracksCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.presentation.HandleFavoritesTracksFromCache
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.frienddetails.data.FriendsDetailsCacheRepository
import com.example.musicapp.frienddetails.domain.FriendDetailsPlaylistsInteractor
import com.example.musicapp.frienddetails.domain.FriendDetailsTracksInteractor
import com.example.musicapp.friends.presentation.HandleFriendsUpdate
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.search.data.SearchRepository
import com.example.musicapp.userplaylists.presentation.BasePlaylistsViewModel
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistCommunication
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistsUiCommunication
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
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
             //   .collectLatest {
               // Log.d("tag", "searchPlylist: ${it.size}")
                handleFriendPlaylistsFromCache.handle(result)
            //}
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