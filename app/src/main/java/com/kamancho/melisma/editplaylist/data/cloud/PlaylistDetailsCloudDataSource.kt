package com.kamancho.melisma.editplaylist.data.cloud

import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.favorites.data.cloud.BaseFavoritesTracksCloudDataSource.Companion.PACKET_SIZE
import com.kamancho.melisma.main.data.cache.AccountDataStore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistDetailsCloudDataSource {

    suspend fun fetchTracks(playlistId: String, ownerId: Int): List<TrackItem>

    suspend fun fetchFavoritesPlaylistById(playlistId: String, ownerId: Int): PlaylistItem

    suspend fun fetchSearchPlaylistById(playlistId: String, ownerId: Int): SearchPlaylistItem

    class Base @Inject constructor(
        private val accountDataStore: AccountDataStore,
        private val service: PlaylistTracksService,
        private val captchaDataStore: CaptchaDataStore,
    ) : PlaylistDetailsCloudDataSource {

        companion object {
             const val packet_size: Int = 5000
        }

        override suspend fun fetchTracks(playlistId: String, ownerId: Int): List<TrackItem> {

            val list = emptyList<TrackItem>().toMutableList()
            var offset = 0
            while (true) {
                val result = service.getPlaylistTracks(
                    accessToken = accountDataStore.token(),
                    owner_id = ownerId.toString(),
                    album_id = playlistId,
                    count = packet_size,
                    offset = offset,
                    captcha_sid = captchaDataStore.captchaId(),
                    captcha_key = captchaDataStore.captchaEnteredData(),
                ).response.items

                if (result.isEmpty()) break

                list.addAll(result)
                offset += packet_size

            }
            return list
        }

        override suspend fun fetchFavoritesPlaylistById(
            playlistId: String,
            ownerId: Int,
        ): PlaylistItem {
            return service.getFavoritePlaylistById(
                accountDataStore.token(),
                ownerId.toString(),
                playlistId,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            ).response
        }

        override suspend fun fetchSearchPlaylistById(
            playlistId: String,
            ownerId: Int,
        ): SearchPlaylistItem {
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