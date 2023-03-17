package com.example.musicapp.main.data

import android.util.Log
import androidx.media3.common.MediaItem
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/
interface TemporaryTracksCache {

    fun readTracks(): List<MediaItem>

    fun saveTracks(list: List<MediaItem>)

   suspend fun map(): List<MediaItem>

    class Base @Inject constructor(): TemporaryTracksCache {
        private val tracks = mutableListOf<MediaItem>()
        private var queryId = 0

        override fun readTracks(): List<MediaItem> = tracks

        override fun saveTracks(list: List<MediaItem>) {
            tracks.clear()
            tracks.addAll(list)
        }

        override suspend fun map(): List<MediaItem> {
            val newQueryId = tracks.hashCode()
            return if(queryId!=newQueryId){
                queryId = newQueryId
                tracks
            }else emptyList()
        }
    }
}