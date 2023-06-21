package com.example.musicapp.search.data

import androidx.media3.common.MediaItem
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.musicapp.app.SpotifyDto.SearchTracks
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.SearchQueryRepository
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.search.data.cloud.SearchTrackService
import com.example.musicapp.searchhistory.data.cache.SearchQueryTransfer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by HP on 29.04.2023.
 **/
interface SearchRepository: SearchQueryRepository {

    suspend fun searchTracksByName(searchTerm: String): Flow<PagingData<MediaItem>>

    class Base @Inject constructor(
        private val service: SearchTrackService,
        private val mapper: SearchTracks.Mapper<List<MediaItem>>,
        private val tokenStore: AccountDataStore,
        private val cachedTracks: TemporaryTracksCache,
        private val handleResponse: HandleResponse<PagingSource.LoadResult<Int, MediaItem>>,
        transfer: SearchQueryTransfer
    ): SearchRepository, SearchQueryRepository.Abstract(transfer) {

        companion object{
            const val page_size = 20
        }
        override suspend fun searchTracksByName(searchTerm: String): Flow<PagingData<MediaItem>> {
            return Pager(
                config = PagingConfig(
                    pageSize = page_size,
                    enablePlaceholders = false,
                    prefetchDistance = 1,
                    initialLoadSize = page_size
                ),
                pagingSourceFactory = { SearchPagingSource(service,searchTerm, mapper,tokenStore,handleResponse ,cachedTracks) }
            ).flow
        }

    }
}