package com.example.musicapp.queue.presenatation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.BottomSheetPlayerViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.TracksRepository
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.CollectCurrentQueue
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.SlideViewPagerCommunication
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
class QueueViewModel @Inject constructor(
   private val playerCommunication: PlayerCommunication,
   tracksCache: TemporaryTracksCache,
   tracksRepository: TracksRepository,
   private val dispatchersList: DispatchersList,
   private val slideViewPagerCommunication: SlideViewPagerCommunication,
): BottomSheetPlayerViewModel(
    playerCommunication,
    tracksCache,
    tracksRepository,
    dispatchersList,
    slideViewPagerCommunication,
), CollectCurrentQueue {

    override suspend fun collectCurrentQueue(
        owner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>,
    ) = playerCommunication.collectCurrentQueue(owner, collector)


}