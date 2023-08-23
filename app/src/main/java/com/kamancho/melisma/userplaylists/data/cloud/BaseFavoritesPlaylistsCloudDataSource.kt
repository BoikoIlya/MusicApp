package com.kamancho.melisma.userplaylists.data.cloud

import com.kamancho.melisma.app.core.FavoritesCloudDataSource
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.main.data.cache.AccountDataStore
import javax.inject.Inject

/**
 * Created by HP on 12.07.2023.
 **/
class BaseFavoritesPlaylistsCloudDataSource @Inject constructor(
    private val service: PlaylistsService,
    private val accountDataStore: AccountDataStore
) : FavoritesCloudDataSource<PlaylistItem> {

    override suspend fun addToFavorites(data: Pair<Int, Int>): Int = 0

    override suspend fun removeFromFavorites(id: Int) {
        service.deletePlaylist(accountDataStore.token(),accountDataStore.ownerId(),id)
    }

    override suspend fun favorites(): List<PlaylistItem> =
        service.getPlaylists(accountDataStore.token(),accountDataStore.ownerId()).response.items
}