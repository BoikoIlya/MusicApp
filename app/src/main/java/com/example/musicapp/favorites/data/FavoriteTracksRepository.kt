package com.example.musicapp.favorites.data

import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.core.TracksRepository
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.trending.domain.TrackDomain
import com.example.testapp.spotifyDto.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
        private val managerResource: ManagerResource
    ) : FavoriteTracksRepository,
        TracksRepository.Abstract(cache, toCacheMapper, toMediaItemMapper, transfer,managerResource) {


        override suspend fun removeTrack(id: String): TracksResult {
             cache.removeTrack(id)
            return TracksResult.Success(message = managerResource.getString(R.string.success_remove_message))
        }

    }
}