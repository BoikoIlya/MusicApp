package com.kamancho.melisma.frienddetails.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.BaseViewModel
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.favorites.presentation.FavoritesTracksCommunication
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.favorites.presentation.HandleFavoritesTracksFromCache
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.frienddetails.data.FriendsDetailsCacheRepository
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
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
            handleFavoritesTracksFromCache.handle(result)
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