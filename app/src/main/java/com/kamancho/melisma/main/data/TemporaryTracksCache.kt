package com.kamancho.melisma.main.data

import androidx.media3.common.MediaItem
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/
interface TemporaryTracksCache {

      fun readCurrentPageTracks(): List<MediaItem>

      suspend fun findTrackPosition(id: String):Int

      suspend fun saveCurrentPageTracks(list: List<MediaItem>)

      suspend fun map(): List<MediaItem>

    suspend fun addPagingData(list: List<MediaItem>, isNewFirstPage: Boolean)

    class Base @Inject constructor(): TemporaryTracksCache{
        private val currentPageTracks = mutableListOf<MediaItem>()
        private var queueId = 0

        override  fun readCurrentPageTracks(): List<MediaItem> =synchronized(currentPageTracks) {
            val newList = emptyList<MediaItem>().toMutableList()
            newList.addAll(currentPageTracks)
            newList
        }

        override suspend fun saveCurrentPageTracks(list: List<MediaItem>) {
            synchronized(currentPageTracks) {
                if (list == currentPageTracks) return

                currentPageTracks.clear()
                currentPageTracks.addAll(list)
            }
        }


        override suspend fun map(): List<MediaItem> = synchronized(currentPageTracks)  {
            val newQueueId = currentPageTracks.hashCode()
            return if(queueId!=newQueueId){
                queueId = newQueueId
                val newList = emptyList<MediaItem>().toMutableList()
                newList.addAll(currentPageTracks)
                newList
            }else emptyList()
        }

        override suspend fun addPagingData(list: List<MediaItem>, isNewFirstPage: Boolean) {
            synchronized(currentPageTracks) {
                if (isNewFirstPage) currentPageTracks.clear()
                currentPageTracks.addAll(list)
            }
        }

        override suspend fun findTrackPosition(id: String): Int   {
              return synchronized(currentPageTracks) {   currentPageTracks.indexOfFirst { it.mediaId == id }}
        }
    }


    object Empty: TemporaryTracksCache{
        override fun readCurrentPageTracks(): List<MediaItem> = emptyList()
        override suspend fun findTrackPosition(id: String): Int = -1
        override suspend fun map(): List<MediaItem>  = emptyList()
        override suspend fun addPagingData(list: List<MediaItem>, isNewFirstPage: Boolean) = Unit
        override suspend fun saveCurrentPageTracks(list: List<MediaItem>)  = Unit

    }
}