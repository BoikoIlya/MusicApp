package com.example.musicapp.main.presentation

import android.app.Application
import android.media.MediaPlayer
import android.provider.Settings.Global
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.PlayerControlsCommunication
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.trending.presentation.MediaControllerWrapper
import kotlinx.coroutines.*
import okhttp3.internal.checkDuration

/**
 * Created by HP on 18.03.2023.
 **/
sealed interface PlayerCommunicationState{

    fun apply(
        playerControls: PlayerControlsCommunication,
        currentQueueCommunication: CurrentQueueCommunication,
        selectedTrackCommunication: SelectedTrackCommunication,
        controller: MediaControllerWrapper,
        singleUiEventCommunication: SingleUiEventCommunication,
        trackDurationCommunication: TrackDurationCommunication
    )

    data class SetQueue(
        private val tracks: List<MediaItem>,
        private val dispatchersList: DispatchersList
    ): PlayerCommunicationState{

       private val scope = CoroutineScope(dispatchersList.io())

        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            controller.setMediaItems(tracks)
            currentQueueCommunication.map(tracks)

            controller.addListener(@UnstableApi object : Player.Listener{

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if(isPlaying) playerControls.map(PlayerControlsState.Play(controller.currentMediaItem!!))
                    else playerControls.map(
                        PlayerControlsState.Pause(
                            controller.currentMediaItem!!
                        ))
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if(playbackState== STATE_READY)
                       trackDurationCommunication.map(controller.contentDuration)
                    if(playbackState!=STATE_ENDED) return
                    controller.seekToDefaultPosition(0)
                    controller.prepare()
                    controller.playWhenReady = true
                }

                override fun onPlayerError(error: PlaybackException) {
                    scope.launch(dispatchersList.io()) {
                        singleUiEventCommunication.map(
                            SingleUiEventState.ShowSnackBar.Error(error.message.toString()))
                        this.cancel()
                    }
                    controller.seekToNextMediaItem()
                    controller.play()
                    super.onPlayerError(error)
                }


                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    playerControls.map(PlayerControlsState.Play(mediaItem?: MediaItem.Builder().build()))
                    selectedTrackCommunication.map(controller.currentMediaItem!!)
                    Log.d("tag", "onMediaItemTransition: ${mediaItem?.mediaMetadata?.title}")
                }
            })
        }

    }

    data class Play(
        private val track: MediaItem,
        private val position: Int,
    ): PlayerCommunicationState{

        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            selectedTrackCommunication.map(track)
            playerControls.map(PlayerControlsState.Play(track))
            controller.seekToDefaultPosition(position)
            controller.prepare()
            controller.playWhenReady = true
        }
    }



    object Pause: PlayerCommunicationState{
        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            controller.pause()
        }
    }

    object Resume: PlayerCommunicationState{
        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            controller.prepare()
            controller.playWhenReady = true
        }

    }


    object Disabled: PlayerCommunicationState{
        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            playerControls.map(PlayerControlsState.Disabled)
            selectedTrackCommunication.map(MediaItem.Builder().build())
        }

    }

    object Next: PlayerCommunicationState{
        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            controller.seekToNextMediaItem()
            controller.playWhenReady = true
        }
    }

    object Previous: PlayerCommunicationState{
        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            controller.seekToPreviousMediaItem()
            controller.playWhenReady = true
        }

    }

    data class RepeatMode(
        private val repeatMode: Int
    ): PlayerCommunicationState{
        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            controller.repeatMode = this.repeatMode
        }

    }


    data class SeekToPosition(
        private val position: Long
            ) : PlayerCommunicationState{
        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            controller.seekTo(position)
        }
    }

    data class ShuffleMode(
        private val mode: Boolean
        ): PlayerCommunicationState{

        companion object{
            const val ENABLE_SHUFFLE = true
            const val DISABLE_SHUFFLE = false
        }

        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            controller.shuffleModeEnabled = mode
        }
    }

    data class AddMediaItems(
        private val newPageMediaItems:  List<MediaItem>,
        private val allQueue: List<MediaItem>
    ): PlayerCommunicationState{
        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: SingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication,
        ) {
            controller.addMediaItems(newPageMediaItems)
            currentQueueCommunication.map(allQueue)
        }

    }

}