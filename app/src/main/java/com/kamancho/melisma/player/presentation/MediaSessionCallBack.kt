package com.kamancho.melisma.player.presentation

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.main.data.TemporaryTracksCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject


@UnstableApi /**
 * Created by HP on 07.03.2023.
 **/
class MediaSessionCallBack @Inject constructor(
    private val temporaryTracksCache: TemporaryTracksCache
) : MediaSession.Callback{



    override fun onSetMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>,
        startIndex: Int,
        startPositionMs: Long
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        return Futures.immediateFuture(MediaSession.MediaItemsWithStartPosition(
            temporaryTracksCache.readCurrentPageTracks(),
            startIndex,
            startPositionMs
        ))
    }

}