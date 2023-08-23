package com.kamancho.melisma.search.data

import androidx.media3.common.MediaItem
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.SearchQueryRepository
import com.kamancho.melisma.app.vkdto.SearchPlaylistsCloud
import com.kamancho.melisma.app.vkdto.TracksCloud
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.data.cache.AccountDataStore
import com.kamancho.melisma.search.data.cloud.PlaylistsCloudToPlaylistUiMapper
import com.kamancho.melisma.search.data.cloud.SearchPlaylistsService
import com.kamancho.melisma.search.data.cloud.SearchTracksService
import com.kamancho.melisma.search.data.cloud.TracksCloudToMediaItemsMapper
import com.kamancho.melisma.searchhistory.data.cache.SearchQueryTransfer
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by HP on 29.04.2023.
 **/
interface SearchRepository<T:Any>: SearchQueryRepository {

    suspend fun search(searchTerm: String): Flow<PagingData<T>>

    abstract class Abstract<N,T:Any> (
        transfer: SearchQueryTransfer
    ): SearchRepository<T>, SearchQueryRepository.Abstract(transfer) {

        companion object{
            const val page_size = 20
        }
        override suspend fun search(searchTerm: String): Flow<PagingData<T>> {
            return Pager(
                config = PagingConfig(
                    pageSize = page_size,
                    enablePlaceholders = false,
                    prefetchDistance = 1,
                    initialLoadSize = page_size
                ),
                pagingSourceFactory = { createPagingSource(searchTerm) }
            ).flow
        }

        protected abstract fun createPagingSource(query: String): SearchPagingSource<N,T>

    }

    class BaseTracks @Inject constructor(
        private val service: SearchTracksService,
        private val mapper: TracksCloudToMediaItemsMapper,
        private val tokenStore: AccountDataStore,
        private val cachedTracks: TemporaryTracksCache,
        private val handleResponse: HandleResponse,
        private val captchaDataStore: CaptchaDataStore,
        transfer: SearchQueryTransfer
    ): SearchRepository<MediaItem>,
        SearchRepository.Abstract<TracksCloud, MediaItem>(transfer){

        override fun createPagingSource(query: String): SearchPagingSource<TracksCloud, MediaItem> =
            MediaItemsSearchPagingSource(
                service,
                query,
                mapper,
                tokenStore,
                handleResponse,
                captchaDataStore,
                cachedTracks
            )
    }

    class BasePlaylists @Inject constructor(
        private val service: SearchPlaylistsService,
        private val mapper: PlaylistsCloudToPlaylistUiMapper,
        private val tokenStore: AccountDataStore,
        private val handleResponse: HandleResponse,
        private val captchaDataStore: CaptchaDataStore,
        transfer: SearchQueryTransfer
    ): SearchRepository<PlaylistUi>,
        SearchRepository.Abstract<SearchPlaylistsCloud, PlaylistUi>(transfer){

        override fun createPagingSource(query: String): SearchPagingSource<SearchPlaylistsCloud, PlaylistUi> =
            PlaylistsSearchPagingSource(
                service,
                query,
                mapper,
                tokenStore,
                handleResponse,
                captchaDataStore
            )
    }

}