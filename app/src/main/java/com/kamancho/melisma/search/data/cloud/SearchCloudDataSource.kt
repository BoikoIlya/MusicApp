package com.kamancho.melisma.search.data.cloud

import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.main.data.cache.AccountDataStore
import com.kamancho.melisma.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 25.10.2023.
 **/
interface SearchCloudDataSource<T> {

    suspend fun fetch(query: String, offset: Int, pageSize: Int): List<T>

    class BaseTracksSearch @Inject constructor(
        private val service: SearchTracksService,
        private val tokenStore: AccountDataStore,
        private val captchaDataStore: CaptchaDataStore,
    ) : SearchCloudDataSource<TrackItem> {

        override suspend fun fetch(query: String, offset: Int, pageSize: Int): List<TrackItem> {
            return service.search(
                tokenStore.token(),
                query,
                pageSize,
                offset,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            ).response.items
        }
    }

    class BasePlaylistsSearch @Inject constructor(
        private val service: SearchPlaylistsService,
        private val tokenStore: AccountDataStore,
        private val captchaDataStore: CaptchaDataStore,
    ) : SearchCloudDataSource<SearchPlaylistItem> {

        override suspend fun fetch(
         query: String,
         offset: Int,
         pageSize: Int,
        ): List<SearchPlaylistItem> {
            return service.search(
                tokenStore.token(),
                query,
                pageSize,
                offset,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            ).response.items
        }
    }
}