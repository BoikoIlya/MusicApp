package com.example.musicapp.player.presentation

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Future
import javax.inject.Inject


/**
 * Created by HP on 07.03.2023.
 **/
class MediaSessionCallBack @Inject constructor() : MediaSession.Callback {

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val updatedMediaItems = mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
        return Futures.immediateFuture(updatedMediaItems)
    }

}