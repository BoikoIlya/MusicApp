package com.kamancho.melisma.creteplaylist.data.cloud

import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.app.vkdto.Response
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.main.data.cache.AccountDataStore
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistDataCloudDataSource {

    suspend fun createPlaylist(title: String, description: String): PlaylistItem

    suspend fun followPlaylist(playlistId: String, ownerId: Int ): Response

    suspend fun editPlaylist(playlistId: String, title: String, description: String)

    suspend fun addToPlaylist(playlistId: String, audioIds: List<Int> )

    suspend fun removeFromPlaylist(playlistId: String, audioIds: List<Int>)

    class Base @Inject constructor(
        private val service: PlaylistDataService,
        private val accountDataStore: AccountDataStore,
        private val mapper: AudioIdsToContentIdsMapper,
        private val captchaDataStore: CaptchaDataStore,

    ): PlaylistDataCloudDataSource {

        override suspend fun createPlaylist(title: String, description: String): PlaylistItem =
            service.createPlaylist(
                accountDataStore.token(),
                accountDataStore.ownerId(),
                title,
                description,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            ).response


        override suspend fun followPlaylist(playlistId: String, ownerId: Int): Response =
            service.followPlaylist(
                accountDataStore.token(),
                ownerId,
                playlistId,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            ).response

        override suspend fun editPlaylist(playlistId: String, title: String, description: String) {
            service.editPlaylist(
                accountDataStore.token(),
                accountDataStore.ownerId(),
                playlistId,
                title,
                description,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            )
        }



        override suspend fun addToPlaylist(playlistId: String, audioIds: List<Int>) {
            if(audioIds.isEmpty()) return
            service.addToPlaylist(
                accountDataStore.token(),
                accountDataStore.ownerId(),
                playlistId,
                mapper.map(audioIds),
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            )
        }

        override suspend fun removeFromPlaylist(playlistId: String, audioIds: List<Int>) {
            if(audioIds.isEmpty()) return
            service.removeFromPlaylist(
                accountDataStore.token(),
                accountDataStore.ownerId(),
                playlistId,
                mapper.map(audioIds),
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            )
        }
    }
}