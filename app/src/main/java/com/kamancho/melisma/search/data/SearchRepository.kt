package com.kamancho.melisma.search.data

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.PagingSource
import com.kamancho.melisma.app.core.SearchQueryRepository
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.app.vkdto.SearchPlaylistsCloud
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.app.vkdto.TracksCloud
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.data.cache.AccountDataStore
import com.kamancho.melisma.search.data.cloud.PlaylistsCloudToPlaylistUiMapper
import com.kamancho.melisma.search.data.cloud.SearchCloudDataSource
import com.kamancho.melisma.search.data.cloud.SearchPlaylistsService
import com.kamancho.melisma.search.data.cloud.SearchTracksService
import com.kamancho.melisma.search.data.cloud.TracksCloudToMediaItemsMapper
import com.kamancho.melisma.searchhistory.data.cache.SearchQueryTransfer
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 29.04.2023.
 **/
interface SearchRepository<T> : SearchQueryRepository {

    suspend fun search(searchTerm: String): List<T>

    abstract class Abstract<T>(
        transfer: SearchQueryTransfer,
        private val pagingSource: PagingSource<T>,
        private val cloud: SearchCloudDataSource<T>,
    ) : SearchRepository<T>, SearchQueryRepository.Abstract(transfer) {

        override suspend fun search(searchTerm: String): List<T> =
            pagingSource.newPage { offset, pageSize ->
                return@newPage cloud.fetch(searchTerm, offset, pageSize)
            }


    }

    class BaseTracks @Inject constructor(
        cloudDataSource: SearchCloudDataSource<TrackItem>,
        pagingSource: PagingSource<TrackItem>,
        transfer: SearchQueryTransfer,
    ) : SearchRepository<TrackItem>,
        SearchRepository.Abstract<TrackItem>(transfer, pagingSource, cloudDataSource)

    class BasePlaylists @Inject constructor(
        cloudDataSource: SearchCloudDataSource<SearchPlaylistItem>,
        pagingSource: PagingSource<SearchPlaylistItem>,
        transfer: SearchQueryTransfer,
    ) : SearchRepository<SearchPlaylistItem>,
        SearchRepository.Abstract<SearchPlaylistItem>(transfer, pagingSource, cloudDataSource)

}