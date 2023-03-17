package com.example.musicapp.trending.core

import androidx.media3.common.MediaItem
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.trending.trending.data.ObjectCreator

/**
 * Created by HP on 17.03.2023.
 **/
class TemporaryTracksCacheTest: TemporaryTracksCache, ObjectCreator() {
    var isNewQuery = false
    val tracks = emptyList<MediaItem>().toMutableList()

    override fun readTracks(): List<MediaItem> = tracks

    override fun saveTracks(list: List<MediaItem>) {
        tracks.addAll(list)
    }

    override suspend fun map(): List<MediaItem> {
        return if(isNewQuery) tracks
        else emptyList<MediaItem>()
    }
}