package com.example.musicapp.app.main.data

import androidx.media3.common.MediaItem
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/
interface TemporaryTracksCache {

    fun readTracks(): List<MediaItem>

    fun saveTracks(list: List<MediaItem>)

    class Base @Inject constructor(): TemporaryTracksCache {
        private val tracks = mutableListOf<MediaItem>()

        override fun readTracks(): List<MediaItem> = tracks

        override fun saveTracks(list: List<MediaItem>) {
            tracks.addAll(list)
        }
    }
}