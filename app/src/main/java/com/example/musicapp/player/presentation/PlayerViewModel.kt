package com.example.musicapp.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.app.core.*
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.main.data.TemporaryTracksCache
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
    private val isSavedCommunication: IsSavedCommunication,
    private val communication: TrackPlaybackPositionCommunication,
    private val dispatchersList: DispatchersList,
    private val controller: MediaControllerWrapper,
    private val favoriteTracksRepository: FavoriteTracksRepository,
    private val mapper: TracksResultToUiEventCommunicationMapper,
    private val bottomSheetCommunication: BottomSheetCommunication,
    private val slideViewPagerCommunication: SlideViewPagerCommunication,
    private val trackDurationCommunication: TrackDurationCommunication,
    temporaryTracksCache: TemporaryTracksCache
):  BottomSheetPlayerViewModel(
    playerCommunication,
    temporaryTracksCache,
    favoriteTracksRepository,
    dispatchersList,
    slideViewPagerCommunication),
    CollectPlayerControls {



    init {
        currentPosition()
    }



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


    fun durationForTextView(
        duration: Long = controller.duration
    ): String{
       val time = if(duration<0) 0 else duration
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(time)
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(time) -
                TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun isShuffleModeEnabled() = controller.shuffleModeEnabled

    fun isRepeatEnabled():Boolean = controller.repeatMode == REPEAT_MODE_ONE

    fun isSaved(url: String) = viewModelScope.launch(dispatchersList.io()) {
        isSavedCommunication.map(favoriteTracksRepository.contains(url))
    }

    fun saveTrack(data: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
        favoriteTracksRepository.insertData(data).map(mapper)
    }

    fun removeTrack(id: String) = viewModelScope.launch(dispatchersList.io()) {
        favoriteTracksRepository.removeTrack(id).map(mapper)
    }

    fun bottomSheetState(newState: Int){
        bottomSheetCommunication.map(newState)
    }
    suspend fun collectIsSaved(
        owner: LifecycleOwner,
        collector: FlowCollector<Boolean>,
    ) = isSavedCommunication.collect(owner, collector)

    suspend fun collectTrackPosition(
        owner: LifecycleOwner,
        collector: FlowCollector<Pair<Int,String>>,
    ) = communication.collect(owner,collector)


    override suspend fun collectPlayerControls(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>,
    ) = playerCommunication.collectPlayerControls(owner,collector)

    suspend fun collectTrackDurationCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<Long>
    ) = trackDurationCommunication.collect(owner, collector)
}