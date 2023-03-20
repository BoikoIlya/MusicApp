package com.example.musicapp.main.presentation

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.example.musicapp.app.core.PlayerControlsCommunication
import com.example.musicapp.trending.presentation.MediaControllerWrapper

/**
 * Created by HP on 18.03.2023.
 **/

sealed interface PlayerCommunicationState{

    fun apply(
        bottomPlayerBarCommunication: PlayerControlsCommunication,
        currentQueueCommunication: CurrentQueueCommunication,
        selectedTrackCommunication: SelectedTrackCommunication,
        controller: MediaControllerWrapper,
    )

    data class SetQueue(
        private val tracks: List<MediaItem>
    ): PlayerCommunicationState{

        override fun apply(
            bottomPlayerBarCommunication: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
        ) {
            controller.setMediaItems(tracks)
            currentQueueCommunication.map(tracks)

            controller.addListener(object : Player.Listener{

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if(isPlaying) bottomPlayerBarCommunication.map(PlayerControlsState.Play(controller.currentMediaItem!!))
                    else bottomPlayerBarCommunication.map(
                        PlayerControlsState.Pause(
                            controller.currentMediaItem!!
                        ))
                }

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Log.d("tag", "${error.message}")
                }



                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    bottomPlayerBarCommunication.map(PlayerControlsState.Play(mediaItem?: MediaItem.Builder().build()))
                    selectedTrackCommunication.map(controller.currentMediaItem!!)
                }
            })
        }

    }

    data class Play(
        private val track: MediaItem,
        private val position: Int,
    ): PlayerCommunicationState{

        override fun apply(
            bottomPlayerBarCommunication: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
        ) {

            selectedTrackCommunication.map(track)
            bottomPlayerBarCommunication.map(PlayerControlsState.Play(track))

            controller.seekToDefaultPosition(position)
            controller.prepare()
            controller.play()

        }
    }



    object Pause: PlayerCommunicationState{
        override fun apply(
            bottomPlayerBarCommunication: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
        ) {
            controller.pause()
        }
    }

    object Resume: PlayerCommunicationState{
        override fun apply(
            bottomPlayerBarCommunication: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
        ) {
            controller.play()
        }

    }


    object Disabled: PlayerCommunicationState{
        override fun apply(
            bottomPlayerBarCommunication: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
        ) {
            bottomPlayerBarCommunication.map(PlayerControlsState.Disabled)
            selectedTrackCommunication.map(MediaItem.Builder().build())
        }

    }

    object Next: PlayerCommunicationState{
        override fun apply(
            bottomPlayerBarCommunication: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
        ) {
            controller.seekToNextMediaItem()
        }
    }

    object Previous: PlayerCommunicationState{
        override fun apply(
            bottomPlayerBarCommunication: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
        ) {
            controller.seekToPreviousMediaItem()
        }

    }

    data class RepeatMode(
        private val repeatMode: Int
    ): PlayerCommunicationState{
        override fun apply(
            bottomPlayerBarCommunication: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
        ) {
            controller.repeatMode = this.repeatMode
        }

    }


    data class SeekToPosition(
        private val position: Long
            ) : PlayerCommunicationState{
        override fun apply(
            bottomPlayerBarCommunication: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
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
            bottomPlayerBarCommunication: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
        ) {
            controller.shuffleModeEnabled = mode
        }
    }


}