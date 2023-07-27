package com.example.musicapp.editplaylist.data.cloud

import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.main.data.cache.AccountDataStore
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistTracksCloudDataSource {

    suspend fun fetch(playlistId: Int): List<TrackItem>

    class Base @Inject constructor(
        private val accountDataStore: AccountDataStore,
        private val service: PlaylistTracksService
    ): PlaylistTracksCloudDataSource {

        override suspend fun fetch(playlistId: Int): List<TrackItem> {
           return service.getPlaylistTracks(
                accountDataStore.token(),
                playlistId,
           ).response.items
        }
    }
}