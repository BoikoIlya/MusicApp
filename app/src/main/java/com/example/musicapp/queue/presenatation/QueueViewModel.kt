package com.example.musicapp.queue.presenatation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BottomSheetPlayerViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TracksResultEmptyMapper
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.CollectCurrentQueue
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.PlayerCommunicationState
import com.example.musicapp.main.presentation.SlideViewPagerCommunication
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
class QueueViewModel @Inject constructor(
    private val playerCommunication: PlayerCommunication,
    tracksInteractor: Interactor<MediaItem,TracksResult>,
    private val dispatchersList: DispatchersList,
    private val slideViewPagerCommunication: SlideViewPagerCommunication,
    private val trackChecker: TrackChecker
): BottomSheetPlayerViewModel(
    playerCommunication,
    tracksInteractor,
    dispatchersList,
    slideViewPagerCommunication,
    TracksResultEmptyMapper(),
    trackChecker
), CollectCurrentQueue {

     fun play(item: MediaItem, position: Int) = viewModelScope.launch(dispatchersList.io()){
            trackChecker.checkIfPlayable(item, playable = {
                withContext(dispatchersList.ui()){
                    super.playerAction(PlayerCommunicationState.Play(item,position))
                }
            })
    }

    override suspend fun collectCurrentQueue(
        owner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>,
    ) = playerCommunication.collectCurrentQueue(owner, collector)


}