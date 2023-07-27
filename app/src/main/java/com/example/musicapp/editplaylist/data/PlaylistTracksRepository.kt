package com.example.musicapp.editplaylist.data

import com.example.musicapp.editplaylist.data.cache.PlaylistTracksCacheDataSource
import com.example.musicapp.editplaylist.data.cloud.PlaylistTracksCloudDataSource
import com.example.musicapp.favorites.data.cloud.TracksCloudToCacheMapper
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistTracksRepository {

    suspend fun fetch(playlistId: Int)

    class Base @Inject constructor(
        private val cloud: PlaylistTracksCloudDataSource,
        private val cache: PlaylistTracksCacheDataSource,
        private val mapper: TracksCloudToCacheMapper
    ): PlaylistTracksRepository {

        override suspend fun fetch(playlistId: Int) {
           val cloudResult = cloud.fetch(playlistId)
            cache.savePlaylistTracks(playlistId,mapper.map(cloudResult))
        }
    }
}