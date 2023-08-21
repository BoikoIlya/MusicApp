package com.example.musicapp.searchplaylistdetails.data.cache

import com.example.musicapp.favorites.data.cache.TrackCache
import javax.inject.Inject

/**
 * Created by HP on 15.08.2023.
 **/
interface SearchPlaylistTracksCacheDataSource {

    suspend fun write(list: List<TrackCache>)

    suspend fun read(): List<TrackCache>

    class Base @Inject constructor(): SearchPlaylistTracksCacheDataSource {

        private val currentList = emptyList<TrackCache>().toMutableList()

        override suspend fun write(list: List<TrackCache>) {
            currentList.clear()
            currentList.addAll(list)
        }

        override suspend fun read(): List<TrackCache> {
            return currentList
        }
    }
}