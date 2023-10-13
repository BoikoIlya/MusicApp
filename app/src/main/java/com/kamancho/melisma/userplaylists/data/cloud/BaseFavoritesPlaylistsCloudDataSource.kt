package com.kamancho.melisma.userplaylists.data.cloud

import com.kamancho.melisma.app.core.FavoritesCloudDataSource
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.favorites.data.cloud.BaseFavoritesTracksCloudDataSource
import com.kamancho.melisma.main.data.cache.AccountDataStore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by HP on 12.07.2023.
 **/
class BaseFavoritesPlaylistsCloudDataSource @Inject constructor(
    private val service: PlaylistsService,
    private val accountDataStore: AccountDataStore,
    private val captchaDataStore: CaptchaDataStore
) : FavoritesCloudDataSource<PlaylistItem> {

    override suspend fun addToFavorites(data: Pair<Int, Int>): Int = 0

    override suspend fun removeFromFavorites(id: Int) {
        service.deletePlaylist(
            accountDataStore.token(),
            accountDataStore.ownerId(),
            id,
            captchaDataStore.captchaId(),
            captchaDataStore.captchaEnteredData()
        )
    }

    override suspend fun favorites(): List<PlaylistItem> =
        service.getPlaylists(
            accountDataStore.token(),
            accountDataStore.ownerId(),
            captchaDataStore.captchaId(),
            captchaDataStore.captchaEnteredData()
        ).response.items
}