package com.kamancho.melisma.app.core

import android.util.Log
import androidx.media3.common.MediaItem
import com.kamancho.melisma.addtoplaylist.domain.SelectedTrackDomain
import com.kamancho.melisma.downloader.data.cache.DownloadTracksCacheDataSource
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.favorites.data.cache.TracksDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.system.measureTimeMillis

/**
 * Created by HP on 16.07.2023.
 **/
interface CacheRepository<T> {

  suspend fun fetch(sortingState: SortingState,playlistId: String): Flow<List<T>>

   suspend fun isDbEmpty(playlistId: String): Boolean



   abstract class Abstract<T>( private val dao: TracksDao): CacheRepository<T>{

       override suspend fun isDbEmpty(playlistId: String): Boolean =
           dao.getAllTracksByTime("",playlistId).first().isEmpty()
   }

    class BaseMediaItem @Inject constructor(
        private val dao: TracksDao,
        private val mapper: ToMediaItemMapper,
        private val downloadsDataSource: DownloadTracksCacheDataSource
    ): Abstract<MediaItem>(dao),CacheRepository<MediaItem> {

        override suspend fun fetch(sortingState: SortingState,playlistId: String): Flow<List<MediaItem>> {
            val downloadedFiles = downloadsDataSource.readListOfFileNamesAndPaths()
            return sortingState.fetch(dao, playlistId).map { list ->
                    list.map { mapper.map(Pair(it, downloadedFiles)) }
                }

        }


    }

    class BaseSelected @Inject constructor(
        private val dao: TracksDao,
        private val mapper: TracksCacheToSelectedTracksDomainMapper
    ): Abstract<SelectedTrackDomain>(dao),CacheRepository<SelectedTrackDomain> {

        override suspend fun fetch(sortingState: SortingState,playlistId: String): Flow<List<SelectedTrackDomain>>{
            return sortingState.fetch(dao,playlistId).map {list-> mapper.map(list)}
        }


    }
}