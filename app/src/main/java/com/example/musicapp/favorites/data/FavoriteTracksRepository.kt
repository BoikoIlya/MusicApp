package com.example.musicapp.favorites.data

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.core.TracksRepository
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TracksCountStore
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.data.cloud.FavoritesService
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.data.cache.AccountDataStore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * Created by HP on 20.03.2023.
 **/
interface FavoriteTracksRepository: TracksRepository {


    suspend fun removeTrack(id: String): TracksResult

    class Base @Inject constructor(
        private val cache: TracksDao,
        toCacheMapper: Mapper<MediaItem, TrackCache>,
        transfer: DataTransfer<MediaItem>,
        private val toMediaItemMapper: Mapper<TrackCache, MediaItem>,
        private val managerResource: ManagerResource,
        private val favoritesService: FavoritesService,
        private val tokenStore: AccountDataStore,
    ) : FavoriteTracksRepository,
        TracksRepository.Abstract(cache, toCacheMapper, toMediaItemMapper, transfer,managerResource) {


        override suspend fun removeTrack(id: String): TracksResult {
             cache.removeTrack(id)
            return TracksResult.Success(message = managerResource.getString(R.string.success_remove_message))
        }

        override suspend fun updateData() {
         val cloudTracksCount = favoritesService.getTracksCount(tokenStore.token(),tokenStore.ownerId()).response

         val result = favoritesService.getFavoritesTracks(tokenStore.token(),cloudTracksCount)

            val calendar = Calendar.getInstance()
            Log.d("tag", "updateData: Size ${result.response.items.size} | Count: ${result.response.count}")
            val mapped = result.response.items.map {
                TrackCache(it.id,it.url, it.title, it.artist, it.album?.thumb?.photo_1200 ?: "",it.album?.thumb?.photo_135?:"", it.album?.title?:"", it.date)
            }

            coroutineScope {
                launch { cache.insertListOfTracks(mapped) }
                launch{ cache.deleteItemsNotInList(mapped.map { it.id })}
            }
        }

    }
}