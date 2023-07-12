package com.example.musicapp.main.presentation

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.PlayerControlsCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_id
import com.example.musicapp.player.presentation.PlayingTrackIdCommunication
import com.example.musicapp.player.presentation.TrackPlaybackPositionCommunication
import com.example.musicapp.trending.presentation.MediaControllerWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 02.07.2023.
 **/


class ControllerListener @Inject constructor(
        private val playerControls: PlayerControlsCommunication,
        private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
        private val selectedTrackCommunication: SelectedTrackCommunication,
        private val trackPlaybackPositionCommunication: TrackPlaybackPositionCommunication,
        private val controller: MediaControllerWrapper,
        private val dispatchersList: DispatchersList,
        private val handlePlayerError: HandlePlayerError,
        private val playingTrackIdCommunication: PlayingTrackIdCommunication
    ): Player.Listener{


        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (isPlaying) playerControls.map(PlayerControlsState.Play(controller.currentMediaItem!!))
            else playerControls.map(PlayerControlsState.Pause(controller.currentMediaItem!!))
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState != Player.STATE_ENDED) return
            controller.seekToDefaultPosition(0)
            controller.prepare()
            controller.playWhenReady = true
        }

        override fun onPlayerError(error: PlaybackException) {
            CoroutineScope(dispatchersList.io()).launch(dispatchersList.io()) {
                handlePlayerError.handle(error)
                    .apply(
                        singleUiEventCommunication,
                        dispatchersList,
                        controller
                    )
                this.cancel()
            }
            super.onPlayerError(error)
        }


        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            playerControls.map(PlayerControlsState.Play(mediaItem!!))
            selectedTrackCommunication.map(mediaItem)
            playingTrackIdCommunication.map(mediaItem.mediaMetadata.extras!!.getInt(track_id))
            trackPlaybackPositionCommunication.clearPosition()
            super.onMediaItemTransition(mediaItem, reason)
        }

        }


