package com.example.musicapp.creteplaylist.data

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.HandleDeleteRequestData
import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.example.musicapp.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import com.example.musicapp.userplaylists.data.cache.BaseFavoritesPlaylistsCacheDataSource
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cache.PlaylistsAndTracksRelation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistDataRepository {

    suspend fun createPlaylist(title: String, description: String,audioIds: List<Int>)

    suspend fun followPlaylist(playlist_id: Int,playlistOwnerId: Int)

    suspend fun editPlaylist(
        playlist_id: Int,
        title: String,
        description: String,
        tracksIdsToAdd: List<Int>,
        tracksIdsToDelete: List<Int>,
    )


    suspend fun removeFromPlaylist(playlist_id: Int, audioIds: List<Int>)

    class Base @Inject constructor(
        private val cloud: PlaylistDataCloudDataSource,
        private val cache: PlaylistDataCacheDataSource,
        private val mapper: PlaylistItem.Mapper<PlaylistCache>,
    ): PlaylistDataRepository {

        override suspend fun createPlaylist(title: String, description: String,audioIds: List<Int>) {
            val cloudResult = cloud.createPlaylist(title,description)
            val cacheData = cloudResult.map(mapper)
            cache.insertPlaylist(cacheData)
            cloud.addToPlaylist(cacheData.playlistId,audioIds)
            cache.addTracksToPlaylist(cacheData.playlistId,audioIds)
        }

        override suspend fun followPlaylist(playlist_id: Int,playlistOwnerId: Int) {
            val cloudResult = cloud.followPlaylist(playlist_id,playlistOwnerId)
            cache.insertPlaylist(cloudResult.map(mapper))
        }

        override suspend fun editPlaylist(
            playlist_id: Int,
            title: String,
            description: String,
            tracksIdsToAdd: List<Int>,
            tracksIdsToDelete: List<Int>,
        ) {
            coroutineScope {
                launch {
                    cloud.editPlaylist(playlist_id, title, description)
                    cache.updatePlaylist(playlist_id, title, description)
                }
                launch {
                    cloud.addToPlaylist(playlist_id,tracksIdsToAdd)
                    cache.addTracksToPlaylist(playlist_id,tracksIdsToAdd)
                }
                launch {
                    cloud.removeFromPlaylist(playlist_id,tracksIdsToDelete)
                    cache.removeTracksFromPlaylist(playlist_id,tracksIdsToDelete)
                }
            }
        }


        override suspend fun removeFromPlaylist(playlist_id: Int, audioIds: List<Int>) {
            try {
                cache.removeTracksFromPlaylist(playlist_id,audioIds)
                cloud.removeFromPlaylist(playlist_id,audioIds)
            }catch (e: Exception){
                cache.addTracksToPlaylist(playlist_id,audioIds)
                throw e
            }

        }
    }
}