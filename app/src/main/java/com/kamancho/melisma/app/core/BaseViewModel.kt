package com.kamancho.melisma.app.core

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.favorites.presentation.UiCommunication
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.CollectSelectedTrack
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.main.presentation.PlayerCommunicationState
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by HP on 21.03.2023.
 **/
abstract class BaseViewModel<T>(
    private val playerCommunication: PlayerCommunication,
    private val tracksCommunication: UiCommunication<T,MediaItem>,
    private val temporaryTracksCache: TemporaryTracksCache,
    private val dispatchersList: DispatchersList,
    private val interactor: Interactor<MediaItem,TracksResult>,
    private val mapper: TracksResultToUiEventCommunicationMapper,
    private val trackChecker: TrackChecker,
): ViewModel(), CollectSelectedTrack, FavoritesViewModel<T,MediaItem>,PlayerAction{


    override fun playerAction(state: PlayerCommunicationState) = playerCommunication.map(state)

    fun saveCurrentPageQueue(queue: List<MediaItem>) = viewModelScope.launch(dispatchersList.io()){
       temporaryTracksCache.saveCurrentPageTracks(queue)
    }

   open fun playMusic(item: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
       trackChecker.checkIfPlayable(item, playable = {

            val queue = temporaryTracksCache.map()

            if(queue.isNotEmpty()){
                val newQueue = queue.map {
                    MediaItem.Builder()
                        .setMediaId(it.mediaId)
                        .build() }
                withContext(dispatchersList.ui()) {
                    playerCommunication.map(PlayerCommunicationState.SetQueue(newQueue,queue))
                }
            }
            val position = temporaryTracksCache.findTrackPosition(item.mediaId)
            withContext(dispatchersList.ui()) {
               playerCommunication.map(PlayerCommunicationState.Play(item,position))
           }
       })

    }


   open fun checkAndAddTrackToFavorites(item: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
        interactor.addToFavoritesIfNotDuplicated(item).map(mapper)
    }






    override suspend fun collectSelectedTrack(
        owner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    ) = playerCommunication.collectSelectedTrack(owner, collector)



    override suspend fun collectData(
        owner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>,
    ) = tracksCommunication.collectData(owner,collector)

    override suspend fun collectState(
        owner: LifecycleOwner,
        collector: FlowCollector<T>
    ) = tracksCommunication.collectState(owner, collector)

    override fun update(loading: Boolean) = Unit

    override suspend fun collectLoading(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>
    ) = Unit
}

interface PlayerAction{
    fun playerAction(state: PlayerCommunicationState)

}


