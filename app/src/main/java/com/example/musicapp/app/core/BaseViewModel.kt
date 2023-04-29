package com.example.musicapp.app.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.CollectSelectedTrack
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.PlayerCommunicationState
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
): ViewModel(), CollectSelectedTrack {

    fun playerAction(state: PlayerCommunicationState) = playerCommunication.map(state)

    fun saveCurrentPageQueue(queue: List<MediaItem>) = viewModelScope.launch(dispatchersList.io()){
        tempTracksCache.saveCurrentPageTracks(queue)
    }

    override suspend fun collectSelectedTrack(
        owner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    ) = playerCommunication.collectSelectedTrack(owner, collector)
}