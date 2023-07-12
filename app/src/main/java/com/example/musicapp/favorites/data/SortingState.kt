package com.example.musicapp.favorites.data

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.presentation.TracksResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by HP on 22.03.2023.
 **/
interface SortingState {

    fun copyObj(query: String): SortingState

    fun fetch(
        cache: TracksDao,
        toMediaItemMapper: Mapper<TrackCache, MediaItem>,
        playlistId: Int
    ): Flow<TracksResult>

     fun handle(
        toMediaItemMapper: Mapper<TrackCache, MediaItem>,
        block:  () ->Flow<List<TrackCache>>,
    ):  Flow<TracksResult>

    abstract class Abstract(): SortingState{

        override  fun handle(
            toMediaItemMapper: Mapper<TrackCache, MediaItem>,
            block:  () ->Flow<List<TrackCache>>,
        ): Flow<TracksResult> {

            return block().map { result ->
                if (result.isEmpty()) TracksResult.Empty
                else TracksResult.Success(result.map { data -> toMediaItemMapper.map(data) })
            }
        }
    }

    data class ByTime(
        private val query: String = ""
    ) : Abstract(){


        override fun copyObj(query: String): SortingState = ByTime(query)


        override  fun fetch(
            cache: TracksDao,
            toMediaItemMapper: Mapper<TrackCache, MediaItem>,
            playlistId: Int
        ): Flow<TracksResult> = super.handle(toMediaItemMapper){ cache.getAllTracksByTime(query,playlistId) }
    }

    data class ByName(
        private val query: String = ""
    ): Abstract(){

        override fun copyObj(query: String): SortingState = ByName(query)


        override  fun fetch(
            cache: TracksDao,
            toMediaItemMapper: Mapper<TrackCache, MediaItem>,
            playlistId: Int
        ):  Flow<TracksResult> = super.handle(toMediaItemMapper){ cache.getTracksByName(query,playlistId) }
    }

    data class ByArtist(
        private val query: String = ""
    ): Abstract(){

        override fun copyObj(query: String): SortingState = ByArtist(query)


        override  fun fetch(
            cache: TracksDao,
            toMediaItemMapper: Mapper<TrackCache, MediaItem>,
            playlistId: Int
        ):  Flow<TracksResult> = super.handle(toMediaItemMapper){ cache.getTracksByArtist(query,playlistId) }
    }

}