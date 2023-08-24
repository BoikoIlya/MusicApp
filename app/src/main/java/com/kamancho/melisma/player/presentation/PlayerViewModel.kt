package com.kamancho.melisma.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.*
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.owner_id
import com.kamancho.melisma.favorites.domain.FavoritesTracksInteractor
import com.kamancho.melisma.main.presentation.*
import com.kamancho.melisma.trending.presentation.MediaControllerWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

 /**d
 * Created by HP on 18.03.2023.
 **/
class PlayerViewModel @Inject constructor(
    private val playerCommunication: PlayerCommunication,
    private val communication: TrackPlaybackPositionCommunication,
    private val dispatchersList: DispatchersList,
    private val controller: MediaControllerWrapper,
    private val favoritesInteractor: FavoritesTracksInteractor,
    private val mapper: TracksResultToUiEventCommunicationMapper,
    private val bottomSheetCommunication: BottomSheetCommunication,
    private val slideViewPagerCommunication: SlideViewPagerCommunication,
    private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val managerResource: ManagerResource,
    private val playingTrackIdCommunication: PlayingTrackIdCommunication,
    private val sleepTimer: SleepTimer,
    trackChecker: TrackChecker,
):  BottomSheetPlayerViewModel(
    playerCommunication,
    favoritesInteractor,
    dispatchersList,
    slideViewPagerCommunication,
    mapper,
    trackChecker
    ),
    CollectPlayerControls {

    private var maxDurationInMillis = 0f

    init {
        currentPosition()
    }

    fun saveMaxDuration(newMaxDuration: Float){
        maxDurationInMillis = newMaxDuration
    }


    fun currentPosition() = viewModelScope.launch(dispatchersList.ui())  {
        while (true){
            delay(500L)
            val durationInMillis = controller.currentPosition.toFloat()
            communication.map(

                Pair(
                    if(durationInMillis>maxDurationInMillis) {
                        maxDurationInMillis
                    }else if(durationInMillis<0f) 0f
                    else durationInMillis,
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



    override fun checkAndAddTrackToFavorites(item: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
            if(favoritesInteractor.equalsWithUserId(item.mediaMetadata.extras!!.getInt(owner_id)))
                singleUiEventCommunication.map(SingleUiEventState.ShowDialog(CantAddTrackFromPlayerMenuDialog()))
            else super.checkAndAddTrackToFavorites(item.buildUpon().setMediaId(item.mediaId.take(9)).build())
    }




     fun launchDeleteItemDialog(newId: String,item: MediaItem): Job = viewModelScope.launch(dispatchersList.io()) {
        if (favoritesInteractor.containsTrackInDb(
                Pair(item.mediaMetadata.title.toString(),
                    item.mediaMetadata.artist.toString())
            )){
                favoritesInteractor.saveItemToTransfer(
                    item.buildUpon().setMediaId(newId.take(9)).build())
                singleUiEventCommunication.map(
                    SingleUiEventState.ShowDialog(
                        DeleteTrackFromPlayerMenuDialog()
                    )
                )
        }
        else
            showSnackBar(SingleUiEventState.ShowSnackBar.Error(managerResource.getString(R.string.not_contain_this_track)))

    }
    
    fun bottomSheetState(newState: Int){
        bottomSheetCommunication.map(newState)
    }

     fun showSnackBar(snackBar: SingleUiEventState.ShowSnackBar) = viewModelScope.launch(dispatchersList.io()) {
         singleUiEventCommunication.map(snackBar)
     }

     private var timerJob: Job?=null
     fun setupSleepTime(timeMinutes:Long ) {
         timerJob?.cancel()
         timerJob = viewModelScope.launch(dispatchersList.io()) {
             singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Success(managerResource.getString(R.string.timer_was_set)))
             sleepTimer.setupTimer(timeMinutes)
         }
     }

     fun disableSleepTimer() = viewModelScope.launch(dispatchersList.io()) {
         timerJob?.cancel()
         singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Success(managerResource.getString(R.string.timer_was_disabled)))
     }


    suspend fun collectTrackPosition(
        owner: LifecycleOwner,
        collector: FlowCollector<Pair<Float,String>>,
    ) = communication.collect(owner,collector)


    override suspend fun collectPlayerControls(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>,
    ) = playerCommunication.collectPlayerControls(owner,collector)


    suspend fun collectPlayingTrackIdCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<String>
    ) = playingTrackIdCommunication.collect(owner, collector)
}