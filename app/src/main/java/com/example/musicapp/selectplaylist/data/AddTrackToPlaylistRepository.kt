package com.example.musicapp.selectplaylist.data

import com.example.musicapp.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.example.musicapp.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
interface AddTrackToPlaylistRepository {

    suspend fun addToPlaylist(playlistId: Int, audioId: Int )

    class Base @Inject constructor(
        private val cloud: PlaylistDataCloudDataSource,
        private val cache: PlaylistDataCacheDataSource,
    ): AddTrackToPlaylistRepository {

        override suspend fun addToPlaylist(playlistId: Int, audioId: Int) {
            cloud.addToPlaylist(playlistId.toString(), listOf(audioId))
            cache.addTracksToPlaylist(playlistId.toString(),listOf(audioId))
        }
    }
}