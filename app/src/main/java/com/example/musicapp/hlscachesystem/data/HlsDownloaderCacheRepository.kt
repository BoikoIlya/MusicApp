package com.example.musicapp.hlscachesystem.data

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.StreamKey
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.hls.offline.HlsDownloader
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist
import androidx.media3.exoplayer.offline.Downloader
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.ToMediaItemMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.Collections
import javax.inject.Inject

/**
 * Created by HP on 08.08.2023.
 **/
interface HlsDownloaderCacheRepository {

    suspend fun download(mediaItem: MediaItem, listener: Downloader.ProgressListener)

    suspend fun remove(mediaItem: MediaItem)

    @UnstableApi class Base @Inject constructor(
        private val cacheDataSourceFactory: CacheDataSource.Factory,
        private val transfer: DataTransfer<String>
    ): HlsDownloaderCacheRepository {

        override suspend fun download(mediaItem: MediaItem,listener: Downloader.ProgressListener) {
                transfer.save(mediaItem.mediaId)
                val mediaWithStreamKey = mediaItem.buildUpon()
                   // .setCustomCacheKey(mediaItem.mediaId)
                    .setStreamKeys(
                        Collections.singletonList(
                            StreamKey(
                                HlsMultivariantPlaylist.GROUP_INDEX_VARIANT, 0))
                    ).build()

                val hlsDownloader = HlsDownloader(mediaWithStreamKey, cacheDataSourceFactory)
                hlsDownloader.download(listener)


        }

        override suspend fun remove(mediaItem: MediaItem) {
            val hlsDownloader = HlsDownloader(mediaItem, cacheDataSourceFactory)
            hlsDownloader.remove()
        }
    }
}