package com.example.musicapp.selectplaylist.data

import com.example.musicapp.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.example.musicapp.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
interface AddTrackToPlaylistRepository {

    suspend fun addToPlaylist(playlist_id: Int, audioId: Int )

    class Base @Inject constructor(
        private val cloud: PlaylistDataCloudDataSource,
        private val cache: PlaylistDataCacheDataSource,
    ): AddTrackToPlaylistRepository {

        override suspend fun addToPlaylist(playlist_id: Int, audioId: Int) {
            cloud.addToPlaylist(playlist_id, listOf(audioId))
            cache.addTracksToPlaylist(playlist_id,listOf(audioId))
        }
    }
}