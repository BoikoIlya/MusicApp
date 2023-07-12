package com.example.musicapp.favorites.data.cloud

import com.example.musicapp.app.core.FavoritesCloudDataSource
import com.example.musicapp.app.vkdto.Item
import com.example.musicapp.main.data.cache.AccountDataStore
import javax.inject.Inject

/**
 * Created by HP on 08.07.2023.
 **/


class BaseFavoritesTracksCloudDataSource @Inject constructor(
    private val accountDataStore: AccountDataStore,
    private val service: FavoritesService
    ): FavoritesCloudDataSource<Item> {

        override suspend fun addToFavorites(ownerAndTrackIds: Pair<Int, Int>): Int
           = service.addTrackToFavorites(
                accountDataStore.token(),
                ownerAndTrackIds.first,
                ownerAndTrackIds.second,
            ).handle()

        override suspend fun removeFromFavorites(id: Int) {
            service.deleteTrackFromFavorites(
                accountDataStore.token(),
                accountDataStore.ownerId(),
                id,
            ).handle()
        }

        override suspend fun favorites(): List<Item> {
            val tracksCount =
                service.getTracksCount(accountDataStore.token(),accountDataStore.ownerId()).handle()

         return service.getFavoritesTracks(accountDataStore.token(),tracksCount).handle()
        }

    }
