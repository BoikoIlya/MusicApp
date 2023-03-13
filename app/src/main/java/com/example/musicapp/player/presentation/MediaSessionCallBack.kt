package com.example.musicapp.player.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.COMMAND_PLAY_PAUSE
import androidx.media3.session.MediaSession
import com.example.musicapp.app.core.BottomPlayerBarCommunicatin
import com.example.musicapp.app.main.presentation.BottomPlayerBarState
import com.example.musicapp.app.main.presentation.PlayerCommunication
import com.example.musicapp.app.main.presentation.PlayerCommunicationState
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by HP on 07.03.2023.
 **/
class MediaSessionCallBack @Inject constructor(
    private val bottomBarCommunication: BottomPlayerBarCommunicatin,
    private val playerCommunication: PlayerCommunication
): MediaSession.Callback {





    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val updatedMediaItems = mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
        return Futures.immediateFuture(updatedMediaItems)
    }



    override fun onPlayerCommandRequest(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        playerCommand: Int
    ): Int {

//        session.player.currentMediaItem?.let {
//           // Log.d("tag", "onPlayerCommandRequest: ")
//            when (playerCommand) {
//                COMMAND_PLAY_PAUSE -> {
//                    bottomBarCommunication.map(
//                        if (session.player.isPlaying) {
//                            Log.d("tag", " pause command in callback")
//                            BottomPlayerBarState.Pause(it)
//                        }
//                        else
//                            BottomPlayerBarState.Resume)
//                }
//            }
//        }
        return super.onPlayerCommandRequest(session, controller, playerCommand)
    }

    override fun onDisconnected(session: MediaSession, controller: MediaSession.ControllerInfo) {
        super.onDisconnected(session, controller)
        playerCommunication.map(PlayerCommunicationState.Disabled)
    }

}