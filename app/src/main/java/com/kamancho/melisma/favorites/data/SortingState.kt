package com.kamancho.melisma.favorites.data

import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.favorites.data.cache.TracksDao
import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 22.03.2023.
 **/
interface SortingState {

    fun copyObj(query: String): SortingState

    fun fetch(
        cache: TracksDao,
        playlistId: String
    ): Flow<List<TrackCache>>


    data class ByTime(
        private val query: String = ""
    ) : SortingState{


        override fun copyObj(query: String): SortingState = ByTime(query)


        override  fun fetch(
            cache: TracksDao,
            playlistId: String
        ): Flow<List<TrackCache>> =  cache.getAllTracksByTime(query,playlistId)
    }

    data class ByName(
        private val query: String = ""
    ): SortingState{

        override fun copyObj(query: String): SortingState = ByName(query)


        override  fun fetch(
            cache: TracksDao,
            playlistId: String
        ):  Flow<List<TrackCache>> =  cache.getTracksByName(query,playlistId)
    }

    data class ByArtist(
        private val query: String = ""
    ): SortingState{

        override fun copyObj(query: String): SortingState = ByArtist(query)


        override  fun fetch(
            cache: TracksDao,
            playlistId: String
        ):  Flow<List<TrackCache>> = cache.getTracksByArtist(query,playlistId)
    }

}