package com.kamancho.melisma.editplaylist.data.cloud

import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.main.data.cache.AccountDataStore
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistDetailsCloudDataSource {

    suspend fun fetchTracks(playlistId: String,ownerId: Int): List<TrackItem>

    suspend fun fetchFavoritesPlaylistById(playlistId: String, ownerId: Int): PlaylistItem

    suspend fun fetchSearchPlaylistById(playlistId: String, ownerId: Int): SearchPlaylistItem

    class Base @Inject constructor(
        private val accountDataStore: AccountDataStore,
        private val service: PlaylistTracksService,
        private val captchaDataStore: CaptchaDataStore
    ): PlaylistDetailsCloudDataSource {

        override suspend fun fetchTracks(playlistId: String,ownerId: Int): List<TrackItem> {
           return service.getPlaylistTracks(
                accountDataStore.token(),
                playlistId,
                ownerId.toString(),
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
           ).response.items
        }

        override suspend fun fetchFavoritesPlaylistById(playlistId: String, ownerId: Int): PlaylistItem {
          return service.getFavoritePlaylistById(
                accountDataStore.token(),
                ownerId.toString(),
                playlistId,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            ).response
        }

        override suspend fun fetchSearchPlaylistById(playlistId: String, ownerId: Int): SearchPlaylistItem {
            return service.getSearchPlaylistById(
                accountDataStore.token(),
                ownerId.toString(),
                playlistId,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            ).response
        }
    }
}