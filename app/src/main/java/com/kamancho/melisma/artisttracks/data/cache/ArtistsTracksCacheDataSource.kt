package com.kamancho.melisma.artisttracks.data.cache

import com.kamancho.melisma.app.vkdto.TrackItem
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
interface ArtistsTracksCacheDataSource {

    suspend fun saveTracks(artistId: String, tracks: List<TrackItem>)

    suspend fun readTracks(artistId: String): List<TrackItem>

    suspend fun clearCache()

    suspend fun readTrackId(): String

    suspend fun saveTrackId(trackId: String)

    class Base @Inject constructor() : ArtistsTracksCacheDataSource {

        private val tracksMap = mutableMapOf<String, List<TrackItem>>()

        private var trackId: String = ""

        override suspend fun saveTracks(artistId: String, tracks: List<TrackItem>) {
            tracksMap[artistId] = tracks
        }

        override suspend fun readTracks(artistId: String): List<TrackItem> {
            val tracks = tracksMap[artistId]
            if (tracks.isNullOrEmpty()) return emptyList()
            val newList = mutableListOf<TrackItem>()
            newList.addAll(tracks)
            return newList
        }

        override suspend fun clearCache() = tracksMap.clear()
        override suspend fun readTrackId(): String = trackId

        override suspend fun saveTrackId(trackId: String) {
            this.trackId = trackId
        }
    }
}