package com.example.musicapp.main.data

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.PlayerCommunicationState
import javax.inject.Inject
import kotlin.random.Random

/**
 * Created by HP on 31.01.2023.
 **/
interface TemporaryTracksCache {

      fun readCurrentPageTracks(): List<MediaItem>

    suspend fun saveCurrentPageTracks(list: List<MediaItem>)
    suspend fun map(): List<MediaItem>

    suspend fun addPagingData(list: List<MediaItem>, isNewFirstPage: Boolean)

    class Base @Inject constructor(): TemporaryTracksCache {
        private val currentPageTracks = mutableListOf<MediaItem>()
        private var queueId = 0

        override  fun readCurrentPageTracks(): List<MediaItem> = currentPageTracks

        override suspend fun saveCurrentPageTracks(list: List<MediaItem>) {
            synchronized(currentPageTracks) {
                if (list == currentPageTracks) return

                currentPageTracks.clear()
                currentPageTracks.addAll(list)
            }
        }


        override suspend fun map(): List<MediaItem> {
            val newQueueId = currentPageTracks.hashCode()
            return if(queueId!=newQueueId){
                queueId = newQueueId
                currentPageTracks
            }else emptyList()
        }

        override suspend fun addPagingData(list: List<MediaItem>, isNewFirstPage: Boolean) {
            if(isNewFirstPage) currentPageTracks.clear()
            currentPageTracks.addAll(list)
        }

    }
}