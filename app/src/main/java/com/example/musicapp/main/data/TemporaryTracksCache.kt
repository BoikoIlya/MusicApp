package com.example.musicapp.main.data

import androidx.media3.common.MediaItem
import javax.inject.Inject
import kotlin.random.Random

/**
 * Created by HP on 31.01.2023.
 **/
interface TemporaryTracksCache {

    suspend fun readCurrentPageTracks(): List<MediaItem>

    suspend fun saveCurrentPageTracks(list: List<MediaItem>)
    suspend fun map(): List<MediaItem>

    class Base @Inject constructor(): TemporaryTracksCache {
        private val currentPageTracks = mutableListOf<MediaItem>()
        private var queueId = 0

        override suspend fun readCurrentPageTracks(): List<MediaItem> = currentPageTracks

        override suspend fun saveCurrentPageTracks(list: List<MediaItem>) {
            currentPageTracks.clear()
            currentPageTracks.addAll(list)
        }


        override suspend fun map(): List<MediaItem> {
            val newQueueId = currentPageTracks.hashCode()
            return if(queueId!=newQueueId){
                queueId = newQueueId
                currentPageTracks
            }else emptyList()
        }

    }
}