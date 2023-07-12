package com.example.musicapp.app.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.CollectPlayerControls
import com.example.musicapp.main.presentation.CollectSelectedTrack
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.PlayerCommunicationState
import com.example.musicapp.main.presentation.PlayerControlsState
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by HP on 21.03.2023.
 **/
abstract class BaseViewModel<T>(
    private val playerCommunication: PlayerCommunication,
    private val tracksCommunication: UiCommunication<T>,
    private val temporaryTracksCache: TemporaryTracksCache,
    private val dispatchersList: DispatchersList,
    private val interactor: Interactor<MediaItem,TracksResult>,
    private val mapper: TracksResultToUiEventCommunicationMapper,
    private val trackChecker: TrackChecker,
): ViewModel(), CollectSelectedTrack, CollectPlayerControls, CollectTracksAndUiState<T>{

   open fun playerAction(state: PlayerCommunicationState) = playerCommunication.map(state)

   open fun saveCurrentPageQueue(queue: List<MediaItem>) = viewModelScope.launch(dispatchersList.io()){
       temporaryTracksCache.saveCurrentPageTracks(queue)
    }

   open fun playMusic(item: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
       trackChecker.checkIfPlayable(item, playable = {

            val queue = temporaryTracksCache.map()
            if(queue.isNotEmpty()){
                val newQueue = mutableListOf<MediaItem>()
                newQueue.addAll(queue)
                withContext(dispatchersList.ui()) {
                    playerCommunication.map(PlayerCommunicationState.SetQueue(newQueue,dispatchersList))
                }
            }
            val position = temporaryTracksCache.readCurrentPageTracks().indexOf(item)

            withContext(dispatchersList.ui()) {
               playerCommunication.map(PlayerCommunicationState.Play(item,position))
           }
       })

    }


   open fun checkAndAddTrackToFavorites(item: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
        interactor.addToFavoritesIfNotDuplicated(item).map(mapper)
    }



    fun shuffle() = viewModelScope.launch(dispatchersList.io()) {
        val newList = emptyList<MediaItem>().toMutableList()
        newList.addAll(temporaryTracksCache.readCurrentPageTracks())
        val shuffled = newList.shuffled()
        temporaryTracksCache.saveCurrentPageTracks(shuffled)
        playMusic(shuffled.first())
    }


    override suspend fun collectSelectedTrack(
        owner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    ) = playerCommunication.collectSelectedTrack(owner, collector)

    override suspend fun collectPlayerControls(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>,
    ) = playerCommunication.collectPlayerControls(owner, collector)


    override suspend fun collectTracks(
        owner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>,
    ) = tracksCommunication.collectTracks(owner,collector)

    override suspend fun collectState(
        owner: LifecycleOwner,
        collector: FlowCollector<T>,
    ) = tracksCommunication.collectState(owner, collector)
}

interface DeleteTrackFromFavorites<T>{
    fun deleteFromFavorites(item: T)
}


interface AddToFavorites<T>{

    fun addToFavorites(item: T)
}

interface ManageFavoriteTracks: AddToFavorites<MediaItem>, DeleteTrackFromFavorites<MediaItem>