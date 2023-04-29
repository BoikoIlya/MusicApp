package com.example.musicapp.favorites.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.CollectSelectedTrack
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.PlayerCommunicationState
import com.example.musicapp.trending.presentation.MediaControllerWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Random
import javax.inject.Inject

/**
 * Created by HP on 20.03.2023.
 **/
class FavoritesViewModel @Inject constructor(
    private val repository: FavoriteTracksRepository,
    private val playerCommunication: PlayerCommunication,
    private val dispatchersList: DispatchersList,
    private val temporaryTracksCache: TemporaryTracksCache,
    private val favoritesTracksCommunication: TracksCommunication,
    private val tracksResultToTracksCommunicationMapper: TracksResultToTracksCommunicationMapper,
    private val tracksResultToSingleUiEventCommunicationMapper: TracksResultToSingleUiEventCommunicationMapper,
): BaseViewModel(playerCommunication, temporaryTracksCache,dispatchersList),
    CollectTracks,CollectSelectedTrack, Remover {

    private var sortState: SortingState = SortingState.ByTime()
    private var query: String = ""

    init {
        fetchData(SortingState.ByTime())
    }

    fun saveQuery(query: String){
        this.query = query
    }

    private var fetching: Job? = null

    fun fetchData(sortingState: SortingState = sortState) {
        fetching?.cancel()
        fetching = viewModelScope.launch(dispatchersList.io()) {
            this@FavoritesViewModel.sortState = sortingState
            repository.fetchData(sortingState.copyObj(query)).collectLatest {
                it.map(tracksResultToTracksCommunicationMapper)
                Log.d("tag", "fetchData: ${sortingState.copyObj(query)}")
            }
        }
    }


    override fun removeItem(id: String) = viewModelScope.launch(dispatchersList.io()) {
        repository.removeTrack(id).map(tracksResultToSingleUiEventCommunicationMapper)
    }


    fun playMusic(item: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
        val queue = temporaryTracksCache.map()
        if(queue.isNotEmpty()){
            val newQueue = mutableListOf<MediaItem>()
            newQueue.addAll(queue)
            withContext(dispatchersList.ui()) {
                playerCommunication.map(PlayerCommunicationState.SetQueue(newQueue,dispatchersList))
            }
        }
        val positionn = temporaryTracksCache.readCurrentPageTracks().indexOfFirst { it.mediaId==item.mediaId }
        withContext(dispatchersList.ui()) {
            playerCommunication.map(PlayerCommunicationState.Play(item,positionn))
        }
    }

    fun shuffle() = viewModelScope.launch(dispatchersList.io()) {
        Log.d("tag", "shuffle: ")
        val newShuffledList = temporaryTracksCache.readCurrentPageTracks()
        favoritesTracksCommunication.showTracks(newShuffledList.shuffled())
    }

    override suspend fun collectState(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoriteTracksUiState>,
    ) = favoritesTracksCommunication.collectState(owner, collector)

    override suspend fun collectTracks(
        owner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>,
    ) = favoritesTracksCommunication.collectTracks(owner,collector)


    override suspend fun collectSelectedTrack(
        owner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    ) = playerCommunication.collectSelectedTrack(owner,collector)
}


interface Remover{

    fun removeItem(id: String): Job

}
