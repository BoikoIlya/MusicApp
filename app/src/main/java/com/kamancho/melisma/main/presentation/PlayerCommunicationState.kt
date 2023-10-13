package com.kamancho.melisma.main.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.PlayerControlsCommunication
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.trending.presentation.MediaControllerWrapper

/**
 * Created by HP on 18.03.2023.
 **/
sealed interface PlayerCommunicationState{

    fun apply(
        playerControls: PlayerControlsCommunication,
        currentQueueCommunication: CurrentQueueCommunication,
        selectedTrackCommunication: SelectedTrackCommunication,
        controller: MediaControllerWrapper,
        singleUiEventCommunication: GlobalSingleUiEventCommunication,
        trackDurationCommunication: TrackDurationCommunication
    )

    data class SetQueue(
        private val tracks: List<MediaItem>,
    ): PlayerCommunicationState{


        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            currentQueueCommunication.map(tracks)
            controller.setMediaItems(tracks)
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
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
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
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
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
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
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
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            controller.stop()
            controller.clearMediaItems()
            currentQueueCommunication.map(emptyList())
            playerControls.map(PlayerControlsState.Disabled)
            selectedTrackCommunication.map(MediaItem.EMPTY)
        }

    }



    object Next: PlayerCommunicationState{
        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
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
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
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
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
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
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
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
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
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
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication,
        ) {
            controller.addMediaItems(newPageMediaItems)
            currentQueueCommunication.map(allQueue)
        }

    }

    data class Replpace(
        private val mediaItem: MediaItem
    ): PlayerCommunicationState{

        override fun apply(
            playerControls: PlayerControlsCommunication,
            currentQueueCommunication: CurrentQueueCommunication,
            selectedTrackCommunication: SelectedTrackCommunication,
            controller: MediaControllerWrapper,
            singleUiEventCommunication: GlobalSingleUiEventCommunication,
            trackDurationCommunication: TrackDurationCommunication
        ) {
            controller.replaceMediaItem(controller.currentMediaItemIndex,mediaItem)
        }
    }

}