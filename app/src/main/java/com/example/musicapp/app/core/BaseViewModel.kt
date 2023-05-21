package com.example.musicapp.app.core

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
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
abstract class BaseViewModel(
    private val playerCommunication: PlayerCommunication,
    private val tempTracksCache: TemporaryTracksCache,
    private val dispatchersList: DispatchersList
): ViewModel(), CollectSelectedTrack, CollectPlayerControls {

    fun playerAction(state: PlayerCommunicationState) = playerCommunication.map(state)

   open fun saveCurrentPageQueue(queue: List<MediaItem>) = viewModelScope.launch(dispatchersList.io()){
        tempTracksCache.saveCurrentPageTracks(queue)
    }

   open fun playMusic(item: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
        val queue = tempTracksCache.map()
        if(queue.isNotEmpty()){
            val newQueue = mutableListOf<MediaItem>()
            newQueue.addAll(queue)
            withContext(dispatchersList.ui()) {
                playerCommunication.map(PlayerCommunicationState.SetQueue(newQueue,dispatchersList))
            }
        }
        val position = tempTracksCache.readCurrentPageTracks().indexOfFirst { it.mediaId==item.mediaId }
        withContext(dispatchersList.ui()) {
            playerCommunication.map(PlayerCommunicationState.Play(item,position))
        }
    }

    override suspend fun collectSelectedTrack(
        owner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    ) = playerCommunication.collectSelectedTrack(owner, collector)

    override suspend fun collectPlayerControls(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>,
    ) = playerCommunication.collectPlayerControls(owner, collector)
}