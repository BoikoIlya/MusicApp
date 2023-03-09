package com.example.musicapp.app.main.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.example.musicapp.app.core.BottomPlayerBarCommunicatin
import com.example.musicapp.player.presentation.PlayerServiceState
import com.example.musicapp.trending.presentation.TrackUi
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/
interface PlayerCommunication: CollectBottomPlayerBar, CollectPlayerService, CollectSelectedTrack {

    interface CollectBottomPlayerBarState: CollectBottomPlayerBar,PlayerCommunication
    interface CollectPlayerServiceState: CollectPlayerService,PlayerCommunication


    fun map(state: PlayerCommunicationState)


    class Base @Inject constructor(
        private val bottomPlayerBarCommunication: BottomPlayerBarCommunicatin,
        private val playerServiceCommunication: PlayerServiceCommunication,
        private val selectedTrackPositionCommunication: SelectedTrackPositionCommunication,
        private val toBottomBarPlayState: TrackUi.Mapper<BottomPlayerBarState.Play>,
        private val toServicePlayState: TrackUi.Mapper<Unit>,
        private val controllerFuture: ListenableFuture<MediaController>
    ): PlayerCommunication, CollectPlayerServiceState, CollectBottomPlayerBarState {

        private val controller: MediaController?
            get() = if (controllerFuture.isDone) controllerFuture.get() else null

        override fun map(state: PlayerCommunicationState) {
            if(controller==null) return
            state.apply(
                bottomPlayerBarCommunication,
                playerServiceCommunication,
                selectedTrackPositionCommunication,
                toBottomBarPlayState,
                toServicePlayState,
                controller!!
            )
        }


        override suspend fun collectBottomBarState(
            owner: LifecycleOwner,
            collector: FlowCollector<BottomPlayerBarState>,
        ) = bottomPlayerBarCommunication.collect(owner,collector)

        override suspend fun collectPlayerServiceState(
            owner: LifecycleOwner,
            collector: FlowCollector<PlayerServiceState>,
        ) = playerServiceCommunication.collect(owner,collector)

        override suspend fun collectSelectedTrack(
            owner: LifecycleOwner,
            collector: FlowCollector<Int>,
        ) = selectedTrackPositionCommunication.collect(owner,collector)

    }



}

interface CollectBottomPlayerBar{

    suspend fun collectBottomBarState(
        owner: LifecycleOwner,
        collector: FlowCollector<BottomPlayerBarState>
    )
}

interface CollectPlayerService{

    suspend fun collectPlayerServiceState(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerServiceState>
    )
}

interface CollectSelectedTrack{
    suspend fun collectSelectedTrack(
        owner: LifecycleOwner,
        collector: FlowCollector<Int>,
    )

}


sealed interface PlayerCommunicationState{

    fun apply(
        bottomPlayerBarCommunication: BottomPlayerBarCommunicatin,
        playerServiceCommunication: PlayerServiceCommunication,
        selectedTrackPositionCommunication: SelectedTrackPositionCommunication,
        toBottomBarPlayState: TrackUi.Mapper<BottomPlayerBarState.Play>,
        toServicePlayState: TrackUi.Mapper<Unit>,
        controller: MediaController
    )

    data class SetQuery(
        private val tracks: List<MediaItem>
    ): PlayerCommunicationState{

        override fun apply(
            bottomPlayerBarCommunication: BottomPlayerBarCommunicatin,
            playerServiceCommunication: PlayerServiceCommunication,
            selectedTrackPositionCommunication: SelectedTrackPositionCommunication,
            toBottomBarPlayState: TrackUi.Mapper<BottomPlayerBarState.Play>,
            toServicePlayState: TrackUi.Mapper<Unit>,
            controller: MediaController,
        ) {
            controller.addMediaItems(tracks)

            controller.addListener(object : Player.Listener{
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Log.d("tag", "${error.message}")
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                }

                override fun onMaxSeekToPreviousPositionChanged(maxSeekToPreviousPositionMs: Long) {
                    super.onMaxSeekToPreviousPositionChanged(maxSeekToPreviousPositionMs)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    selectedTrackPositionCommunication.map(controller.currentMediaItemIndex /*ItemPositionState.UpdateRecyclerViewSelectedItem(controller.currentMediaItemIndex)*/)
                }
            })
        }

    }

    data class Play(
        private val track: MediaItem,
        private val position: Int
    ): PlayerCommunicationState{

        override fun apply(
            bottomPlayerBarCommunication: BottomPlayerBarCommunicatin,
            playerServiceCommunication: PlayerServiceCommunication,
            selectedTrackPositionCommunication: SelectedTrackPositionCommunication,
            toBottomBarPlayState: TrackUi.Mapper<BottomPlayerBarState.Play>,
            toServicePlayState: TrackUi.Mapper<Unit>,
            controller: MediaController
        ) {
            selectedTrackPositionCommunication.map(position)
            bottomPlayerBarCommunication.map(BottomPlayerBarState.Play(track))///////////////////////
            //playerServiceCommunication.map(track.map(toServicePlayState))

            //position.apply { if(it!=0) controller.seekToDefaultPosition(it) else controller.seekToDefaultPosition() }

            controller.seekToDefaultPosition(position)

            controller.prepare()
            controller.play()


        }
    }

    data class Pause(
        private val track: MediaItem,
        ): PlayerCommunicationState{
        override fun apply(
            bottomPlayerBarCommunication: BottomPlayerBarCommunicatin,
            playerServiceCommunication: PlayerServiceCommunication,
            selectedTrackPositionCommunication: SelectedTrackPositionCommunication,
            toBottomBarPlayState: TrackUi.Mapper<BottomPlayerBarState.Play>,
            toServicePlayState: TrackUi.Mapper<Unit>,
            controller: MediaController
        ) {
            Log.d("tag", "call pause in communication")
            controller.pause()
            bottomPlayerBarCommunication.map(BottomPlayerBarState.Pause(track))
            //playerServiceCommunication.map(PlayerServiceState.Pause)

        }
    }

    data class Resume(
        private val track: MediaItem,
    ): PlayerCommunicationState{
        override fun apply(
            bottomPlayerBarCommunication: BottomPlayerBarCommunicatin,
            playerServiceCommunication: PlayerServiceCommunication,
            selectedTrackPositionCommunication: SelectedTrackPositionCommunication,
            toBottomBarPlayState: TrackUi.Mapper<BottomPlayerBarState.Play>,
            toServicePlayState: TrackUi.Mapper<Unit>,
            controller: MediaController
        ) {
            //playerServiceCommunication.map(PlayerServiceState.Resume)
            bottomPlayerBarCommunication.map(BottomPlayerBarState.Play(track))
            controller.play()
        }

    }

    object Disabled: PlayerCommunicationState{
        override fun apply(
            bottomPlayerBarCommunication: BottomPlayerBarCommunicatin,
            playerServiceCommunication: PlayerServiceCommunication,
            selectedTrackPositionCommunication: SelectedTrackPositionCommunication,
            toBottomBarPlayState: TrackUi.Mapper<BottomPlayerBarState.Play>,
            toServicePlayState: TrackUi.Mapper<Unit>,
            controller: MediaController
        ) {
            bottomPlayerBarCommunication.map(BottomPlayerBarState.Disabled)
            //playerServiceCommunication.map(PlayerServiceState.Disabled)
            selectedTrackPositionCommunication.map(-1)
        }

    }
}