package com.kamancho.melisma.main.presentation

import android.media.AudioFocusRequest
import android.media.AudioManager
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.PlayerControlsCommunication
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.track_id
import com.kamancho.melisma.player.presentation.PlayingTrackIdCommunication
import com.kamancho.melisma.player.presentation.TrackPlaybackPositionCommunication
import com.kamancho.melisma.trending.presentation.MediaControllerWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 02.07.2023.
 **/

interface ControllerListener: Player.Listener {

    class Base @Inject constructor(
        private val playerControls: PlayerControlsCommunication,
        private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
        private val selectedTrackCommunication: SelectedTrackCommunication,
        private val trackPlaybackPositionCommunication: TrackPlaybackPositionCommunication,
        private val controller: MediaControllerWrapper,
        private val dispatchersList: DispatchersList,
        private val handlePlayerError: HandlePlayerError,
        private val playingTrackIdCommunication: PlayingTrackIdCommunication,
        private val audioManager: AudioManager,
        private val audioFocusChangeListener: AudioFocusRequest
    ) : ControllerListener {


        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (isPlaying) {playerControls.map(
                PlayerControlsState.Play(
                    controller.currentMediaItem ?: MediaItem.EMPTY
                )
            )
                audioManager.requestAudioFocus(audioFocusChangeListener)
            }
            else playerControls.map(
                PlayerControlsState.Pause(
                    controller.currentMediaItem ?: MediaItem.EMPTY
                )
            )
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
            if(reason==MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED) return
            playerControls.map(PlayerControlsState.Play(mediaItem!!))
            selectedTrackCommunication.map(mediaItem)
            playingTrackIdCommunication.map(mediaItem.mediaId)
            trackPlaybackPositionCommunication.clearPosition()
            super.onMediaItemTransition(mediaItem, reason)
        }

    }
}

