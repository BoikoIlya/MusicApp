package com.example.musicapp.search.data

import androidx.media3.common.MediaItem
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.musicapp.app.SpotifyDto.SearchTracks
import com.example.musicapp.main.data.cache.TokenStore
import com.example.musicapp.search.data.cloud.SearchTrackService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by HP on 29.04.2023.
 **/
interface SearchRepository {

    suspend fun searchTracksByName(searchTerm: String): Flow<PagingData<MediaItem>>

    class Base @Inject constructor(
        private val service: SearchTrackService,
        private val mapper: SearchTracks.SearchTracksToMediaItemMapper,
        private val tokenStore: TokenStore
    ): SearchRepository {

        companion object{
            private const val page_size = 30
        }
        override suspend fun searchTracksByName(searchTerm: String): Flow<PagingData<MediaItem>> {
            return Pager(
                config = PagingConfig(
                    pageSize = page_size,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { SearchPagingSource(service,searchTerm, mapper,tokenStore) }
            ).flow
        }
    }
}