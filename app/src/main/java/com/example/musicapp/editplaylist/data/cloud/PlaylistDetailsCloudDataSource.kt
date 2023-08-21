package com.example.musicapp.editplaylist.data.cloud

import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.app.vkdto.SearchPlaylistItem
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.captcha.data.cache.CaptchaDataStore
import com.example.musicapp.main.data.cache.AccountDataStore
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