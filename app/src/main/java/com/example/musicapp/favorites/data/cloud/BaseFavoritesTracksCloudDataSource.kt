package com.example.musicapp.favorites.data.cloud

import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.FavoritesCloudDataSource
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.captcha.data.cache.CaptchaDataStore
import com.example.musicapp.main.data.cache.AccountDataStore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by HP on 08.07.2023.
 **/


class BaseFavoritesTracksCloudDataSource @Inject constructor(
    private val accountDataStore: AccountDataStore,
    private val service: FavoritesService,
    private val captchaDataStore: CaptchaDataStore,
    private val dispatchersList: DispatchersList
    ): FavoritesCloudDataSource<TrackItem> {

    companion object{
        const val PACKET_SIZE: Int = 3000
    }

        override suspend fun addToFavorites(data: Pair<Int, Int>): Int
           = service.addTrackToFavorites(
                accountDataStore.token(),
                data.first,
                data.second,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            ).response

        override suspend fun removeFromFavorites(id: Int) {
            service.deleteTrackFromFavorites(
                accountDataStore.token(),
                accountDataStore.ownerId(),
                id,
                captchaDataStore.captchaId(),
                captchaDataStore.captchaEnteredData()
            ).response
        }

        override suspend fun favorites(): List<TrackItem> {
            val tracksCount =
                service.getTracksCount(
                    accountDataStore.token(),
                    //"188607073",
                    accountDataStore.ownerId(),
                    captchaDataStore.captchaId(),
                    captchaDataStore.captchaEnteredData()
                ).response


            val totalPackets = (tracksCount + PACKET_SIZE - 1) / PACKET_SIZE

            return coroutineScope {

                (0 until totalPackets).map { batchIndex ->

                    val offset = batchIndex * PACKET_SIZE

                    async(dispatchersList.io()) {
                        service.getTracks(
                            accessToken = accountDataStore.token(),
                            //owner_id =     "188607073",
                            owner_id = accountDataStore.ownerId(),
                            count = PACKET_SIZE,
                            offset = offset,
                            captcha_sid = captchaDataStore.captchaId(),
                            captcha_key = captchaDataStore.captchaEnteredData(),
                        ).response.items
                    }
                }.awaitAll().flatten()
            }

        }

    }
