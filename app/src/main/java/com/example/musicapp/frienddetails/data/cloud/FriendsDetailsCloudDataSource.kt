package com.example.musicapp.frienddetails.data.cloud

import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.app.vkdto.SearchPlaylistItem
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.captcha.data.cache.CaptchaDataStore
import com.example.musicapp.favorites.data.cloud.BaseFavoritesTracksCloudDataSource
import com.example.musicapp.favorites.data.cloud.BaseFavoritesTracksCloudDataSource.Companion.PACKET_SIZE
import com.example.musicapp.favorites.data.cloud.FavoritesService
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.userplaylists.data.cloud.FriendPlaylistsService
import com.example.musicapp.userplaylists.data.cloud.PlaylistsService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendsDetailsCloudDataSource {

    suspend fun fetchTracks(ownerId: String): List<TrackItem>

    suspend fun fetchPlaylists(ownerId: String): List<SearchPlaylistItem>

    class Base @Inject constructor(
        private val service: FavoritesService,
        private val accountDataStore: AccountDataStore,
        private val captchaDataStore: CaptchaDataStore,
        private val dispatchersList: DispatchersList,
        private val friendPlaylistsService: FriendPlaylistsService
    ): FriendsDetailsCloudDataSource {

        override suspend fun fetchTracks(ownerId: String): List<TrackItem> {
            val tracksCount =
                service.getTracksCount(
                    accountDataStore.token(),
                    ownerId,
                    captchaDataStore.captchaId(),
                    captchaDataStore.captchaEnteredData()
                ).response


            var totalPackets = (tracksCount + PACKET_SIZE - 1) / PACKET_SIZE

            if(totalPackets==0) totalPackets = 1

            return coroutineScope {

                (0 until totalPackets).map { batchIndex ->

                    val offset = batchIndex * PACKET_SIZE

                    async(dispatchersList.io()) {
                        service.getTracks(
                            accessToken = accountDataStore.token(),
                            owner_id = ownerId,
                            count = PACKET_SIZE,
                            offset = offset,
                            captcha_sid = captchaDataStore.captchaId(),
                            captcha_key = captchaDataStore.captchaEnteredData(),
                        ).response.items
                    }
                }.awaitAll().flatten()
            }
        }

        override suspend fun fetchPlaylists(ownerId: String): List<SearchPlaylistItem>
            = friendPlaylistsService.getPlaylists(
            accessToken = accountDataStore.token(),
            owner_id = ownerId,
        ).response.items

    }
}