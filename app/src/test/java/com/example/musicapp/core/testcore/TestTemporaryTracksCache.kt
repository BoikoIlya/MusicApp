package com.example.musicapp.core.testcore

import androidx.media3.common.MediaItem
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.trending.data.ObjectCreator

/**
 * Created by HP on 17.03.2023.
 **/
class TestTemporaryTracksCache: TemporaryTracksCache, ObjectCreator() {
    var isNewQuery = false
    val tracks = emptyList<MediaItem>().toMutableList()

    override suspend fun readCurrentPageTracks(): List<MediaItem> = tracks

    override suspend fun saveCurrentPageTracks(list: List<MediaItem>) {
        tracks.addAll(list)
    }

    override suspend fun map(): List<MediaItem> {
        return if(isNewQuery) tracks
        else emptyList<MediaItem>()
    }
}