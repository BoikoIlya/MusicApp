package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import com.example.musicapp.addtoplaylist.domain.SelectedTrackDomain
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.data.cache.TracksDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
interface CachedTracksRepository<T> {

    fun fetch(sortingState: SortingState,playlistId: Int): Flow<List<T>>

   suspend fun isDbEmpty(playlistId: Int): Boolean

   abstract class Abstract<T>( private val dao: TracksDao): CachedTracksRepository<T>{

       override suspend fun isDbEmpty(playlistId: Int): Boolean =
           dao.getAllTracksByTime("",playlistId).first().isEmpty()
   }

    class BaseMediaItem @Inject constructor(
        private val dao: TracksDao,
        private val mapper: ToMediaItemMapper
    ): Abstract<MediaItem>(dao),CachedTracksRepository<MediaItem> {

        override fun fetch(sortingState: SortingState,playlistId: Int): Flow<List<MediaItem>> =
            sortingState.fetch(dao,playlistId).map {list-> list.map{ mapper.map(it) }}

    }

    class BaseSelected @Inject constructor(
        private val dao: TracksDao,
        private val mapper: TracksCacheToSelectedTracksDomainMapper
    ): Abstract<SelectedTrackDomain>(dao),CachedTracksRepository<SelectedTrackDomain> {

        override fun fetch(sortingState: SortingState,playlistId: Int): Flow<List<SelectedTrackDomain>> =
            sortingState.fetch(dao,playlistId).map {list-> mapper.map(list)}

    }
}