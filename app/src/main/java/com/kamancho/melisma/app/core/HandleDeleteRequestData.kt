package com.kamancho.melisma.app.core

import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import javax.inject.Inject

/**
 * Created by HP on 08.07.2023.
 **/
interface HandleDeleteRequestData<T> {

    suspend fun  handle(
        id: Int,
        block: suspend () -> Unit,
        error: suspend (T)->Unit
    )

    abstract class Abstract<T>(
        private val cacheDataSource: FavoritesCacheDataSource<T>
    ):  HandleDeleteRequestData<T>{

        override suspend fun  handle(id: Int,block: suspend () -> Unit, error: suspend (T) -> Unit) {
            val item = cacheDataSource.getById(id)
            try {
               return block.invoke()
            } catch (e: Exception) {
                error.invoke(item)
                throw e
            }
        }

    }
}

class HandleDeleteTrackRequest @Inject constructor(
    dataSource: FavoritesCacheDataSource<TrackCache>
): HandleDeleteRequestData<TrackCache>,HandleDeleteRequestData.Abstract<TrackCache>(dataSource)

class HandleDeletePlaylistRequest @Inject constructor(
    dataSource: FavoritesCacheDataSource<PlaylistCache>
): HandleDeleteRequestData<PlaylistCache>,HandleDeleteRequestData.Abstract<PlaylistCache>(dataSource)

