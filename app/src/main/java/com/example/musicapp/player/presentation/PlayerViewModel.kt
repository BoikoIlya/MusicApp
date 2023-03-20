package com.example.musicapp.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.main.presentation.*
import com.example.musicapp.trending.presentation.MediaControllerWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@UnstableApi /**d
 * Created by HP on 18.03.2023.
 **/
class PlayerViewModel @Inject constructor(
    private val playerCommunication: PlayerCommunication,
    //private val controllerFuture: ListenableFuture<MediaController>,
    private val communication: TrackPlaybackPositionCommunication,
    private val dispatchersList: DispatchersList,
    private val controller: MediaControllerWrapper
): ViewModel(), CollectCurrentQueue, CollectSelectedTrack, CollectPlayerControls {

//    private val controller: MediaController?
//        get() = if (controllerFuture.isDone) controllerFuture.get() else null

    init {
        currentPosition()
    }

    fun playerAction(state: PlayerCommunicationState) = playerCommunication.map(state)

    fun currentPosition() = viewModelScope.launch(dispatchersList.ui())  {
        while (true){
            delay(10L)
            communication.map(
                Pair(
                    controller.currentPosition.toInt(),
                    durationForTextView(controller.currentPosition)
                )
            )
        }
    }

    fun durationSeekBar(): Int = controller.duration.toInt()

    fun durationForTextView(time: Long = controller.duration?:0): String{
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(time)
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(time) -
                TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun isShuffleModeEnabled() = controller.shuffleModeEnabled

    fun isRepeatEnabled():Boolean = controller.repeatMode == REPEAT_MODE_ONE

    suspend fun collectTrackPosition(
        owner: LifecycleOwner,
        collector: FlowCollector<Pair<Int,String>>,
    ) = communication.collect(owner,collector)

    override suspend fun collectCurrentQueue(
        owner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>,
    ) = playerCommunication.collectCurrentQueue(owner, collector)

    override suspend fun collectSelectedTrack(
        owner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    ) = playerCommunication.collectSelectedTrack(owner, collector)

    override suspend fun collectPlayerControls(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>,
    ) = playerCommunication.collectPlayerControls(owner,collector)
}