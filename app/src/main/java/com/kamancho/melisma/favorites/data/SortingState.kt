package com.kamancho.melisma.favorites.data

import com.kamancho.melisma.app.core.PagingSource
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.favorites.data.cache.TracksDao
import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 22.03.2023.
 **/
interface SortingState {

    fun copyObj(query: String): SortingState

   suspend fun fetch(
        cache: TracksDao,
        playlistId: String,
        pagingSource: PagingSource<TrackCache>
    ): Flow<List<TrackCache>>


    abstract class Abstract(
        private val query: String
    ): SortingState {

        override suspend fun fetch(
            cache: TracksDao,
            playlistId: String,
            pagingSource: PagingSource<TrackCache>
        ): Flow<List<TrackCache>> {

            return pagingSource.newPageFlow { offset, pageSize ->
                fetchFromDb(cache,query, offset, pageSize,playlistId)
            }

        }

        protected abstract suspend fun fetchFromDb(
            cache: TracksDao,
            query: String,
            offset: Int,
            pageSize: Int,
            playlistId: String
        ): Flow<List<TrackCache>>
    }

    data class ByTime(
        private val query: String = "",
    ) : Abstract(query){


        override fun copyObj( query: String): SortingState = ByTime(query)



        override suspend fun fetchFromDb(
            cache: TracksDao,
            query: String,
            offset: Int,
            pageSize: Int,
            playlistId: String,
        ): Flow<List<TrackCache>> =  cache.getAllTracksByTime(query,playlistId,pageSize,offset)


    }

    data class ByName(
        private val query: String = "",
    ): Abstract(query){

        override fun copyObj(query: String): SortingState = ByName(query)


        override suspend fun fetchFromDb(
            cache: TracksDao,
            query: String,
            offset: Int,
            pageSize: Int,
            playlistId: String,
        ): Flow<List<TrackCache>>  = cache.getTracksByName(query,playlistId,pageSize,offset)

    }

    data class ByArtist(
        private val query: String = "",
    ): Abstract(query){


        override fun copyObj(query: String): SortingState = ByArtist(query)


        override suspend fun fetchFromDb(
            cache: TracksDao,
            query: String,
            offset: Int,
            pageSize: Int,
            playlistId: String,
        ): Flow<List<TrackCache>>  = cache.getTracksByArtist(query,playlistId,pageSize,offset)
    }

}