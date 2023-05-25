package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.presentation.TracksResult
import kotlinx.coroutines.flow.Flow


/**
 * Created by HP on 27.01.2023.
 **/
interface TracksRepository {

     fun fetchData(state: SortingState):  Flow<TracksResult>

    suspend fun checkInsertData(data: MediaItem): TracksResult

    suspend fun insertData(data: MediaItem): TracksResult

    suspend fun contains(id: String): Boolean

    abstract class Abstract(
        private val cache: TracksDao,
        private val toCacheMapper: Mapper<MediaItem, TrackCache>,
        private val toMediaItemMapper: Mapper<TrackCache, MediaItem>,
        private val transfer: DataTransfer<MediaItem>,
        private val managerResource: ManagerResource
    ): TracksRepository{

        override fun fetchData(state: SortingState): Flow<TracksResult> = state.fetch(cache,toMediaItemMapper)

        override suspend fun checkInsertData(data: MediaItem): TracksResult {
            val item = cache.contains(data.mediaId)
            return if(item==null){
                insertData(data)
                TracksResult.Success(message = managerResource.getString(R.string.success_add_message))
            }else {
                transfer.save(data)
                TracksResult.Duplicate
            }
        }

        override suspend fun insertData(data: MediaItem): TracksResult {
            cache.insertTrack(toCacheMapper.map(data))
            return TracksResult.Success(message = managerResource.getString(R.string.success_add_message))
        }

        override suspend fun contains(id: String): Boolean = cache.contains(id)!=null

    }
}