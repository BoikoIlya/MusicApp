package com.example.musicapp.player.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.app.core.*
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.owner_id
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_id
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import com.example.musicapp.favorites.presentation.DeleteDialogFragment
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.*
import com.example.musicapp.trending.presentation.MediaControllerWrapper
import kotlinx.coroutines.Job
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
    trackChecker: TrackChecker,
    temporaryTracksCache: TemporaryTracksCache
):  BottomSheetPlayerViewModel(
    playerCommunication,
    temporaryTracksCache,
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
                    if(durationInMillis>maxDurationInMillis) maxDurationInMillis else durationInMillis,
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
            else super.checkAndAddTrackToFavorites(item)
    }




     fun launchDeleteItemDialog(newId: Int,item: MediaItem): Job = viewModelScope.launch(dispatchersList.io()) {
        if (favoritesInteractor.containsTrackInDb(
                Pair(item.mediaMetadata.title.toString(),
                    item.mediaMetadata.artist.toString())
            )){
               item.mediaMetadata.extras?.putInt(track_id,newId)
                favoritesInteractor.saveDeletingItem(item)
                singleUiEventCommunication.map(
                    SingleUiEventState.ShowDialog(
                        DeleteTrackFromPlayerMenuDialog()
                    )
                )
        }
        else
            singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(
                managerResource.getString(R.string.not_contain_this_track)))

    }
    
    fun bottomSheetState(newState: Int){
        bottomSheetCommunication.map(newState)
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
        collector: FlowCollector<Int>
    ) = playingTrackIdCommunication.collect(owner, collector)
}