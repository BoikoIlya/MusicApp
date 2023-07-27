package com.example.musicapp.app.core

import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
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

