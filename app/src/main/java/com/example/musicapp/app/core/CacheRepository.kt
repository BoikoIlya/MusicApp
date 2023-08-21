package com.example.musicapp.app.core

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.addtoplaylist.domain.SelectedTrackDomain
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.data.cache.TracksDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.log
import kotlin.system.measureTimeMillis

/**
 * Created by HP on 16.07.2023.
 **/
interface CacheRepository<T> {

    fun fetch(sortingState: SortingState,playlistId: String): Flow<List<T>>

   suspend fun isDbEmpty(playlistId: String): Boolean


   abstract class Abstract<T>( private val dao: TracksDao): CacheRepository<T>{

       override suspend fun isDbEmpty(playlistId: String): Boolean =
           dao.getAllTracksByTime("",playlistId).first().isEmpty()
   }

    class BaseMediaItem @Inject constructor(
        private val dao: TracksDao,
        private val mapper: ToMediaItemMapper
    ): Abstract<MediaItem>(dao),CacheRepository<MediaItem> {

        override fun fetch(sortingState: SortingState,playlistId: String): Flow<List<MediaItem>> {
               return sortingState.fetch(dao, playlistId).map { list ->
                  list.map { mapper.map(it) } }
        }

    }

    class BaseSelected @Inject constructor(
        private val dao: TracksDao,
        private val mapper: TracksCacheToSelectedTracksDomainMapper
    ): Abstract<SelectedTrackDomain>(dao),CacheRepository<SelectedTrackDomain> {

        override fun fetch(sortingState: SortingState,playlistId: String): Flow<List<SelectedTrackDomain>> =
            sortingState.fetch(dao,playlistId).map {list-> mapper.map(list)}

    }
}