package com.example.musicapp.frienddetails.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.CacheRepository
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.presentation.FavoritesTracksCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.presentation.HandleFavoritesTracksFromCache
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.frienddetails.data.FriendsDetailsCacheRepository
import com.example.musicapp.frienddetails.domain.FriendDetailsTracksInteractor
import com.example.musicapp.friends.presentation.HandleFriendsUpdate
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.search.data.SearchRepository
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
class FriendTracksViewModel @Inject constructor(
    playerCommunication: PlayerCommunication,
    private val dispatchersList: DispatchersList,
    temporaryTracksCache: TemporaryTracksCache,
    favoritesInteractor: Interactor<MediaItem, TracksResult>,
    mapper: TracksResultToUiEventCommunicationMapper,
    trackChecker: TrackChecker,
    private val handleFriendsUpdate: HandleFriendsTracksUpdate,
    private val cacheRepository:  FriendsDetailsCacheRepository,
    private val communication: FavoritesTracksCommunication,
    private val handleFavoritesTracksFromCache: HandleFavoritesTracksFromCache,
    private val searchQueryFriendCommunication: SearchQueryFriendCommunication
): BaseViewModel<FavoritesUiState>(
    playerCommunication,
    communication,
    temporaryTracksCache,
    dispatchersList,
    favoritesInteractor,
    mapper,
    trackChecker
),FriendsDataViewModel {

    init {
        search("")
        update(false)
    }

    override fun update(loading: Boolean) {
        viewModelScope.launch(dispatchersList.io()){
            handleFriendsUpdate.handle(loading){ !cacheRepository.isFriendHaveTracks() }
            search("")
        }
    }

    private val searchJob: Job?=null

   override fun search(query: String){
        searchJob?.cancel()

        viewModelScope.launch(dispatchersList.io()){
           val result = cacheRepository.searchTracks(query)
               //.collectLatest {
                    handleFavoritesTracksFromCache.handle(result)
            //}
        }
    }

    override fun checkAndAddTrackToFavorites(item: MediaItem): Job {
        //because first 9 digits is tracks id and other is playlist id, that was add
        //to avoid tracks collision, because ids is not unic
        val newItem = item.buildUpon().setMediaId(item.mediaId.take(9)).build()
        return super.checkAndAddTrackToFavorites(newItem)
    }

    override suspend fun collectSearchQuery(
        owner: LifecycleOwner,
        collector: FlowCollector<String>
    ) = searchQueryFriendCommunication.collect(owner, collector)

    override suspend fun collectLoading(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>
    ) = communication.collectLoading(owner, collector)
}