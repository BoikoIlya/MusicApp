package com.example.musicapp.creteplaylist.data.cloud

import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.favorites.data.cloud.TrackIdResponse
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.main.di.AppModule
import com.example.musicapp.userplaylists.data.cloud.PlaylistDataResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistDataCloudDataSource {

    suspend fun createPlaylist(title: String, description: String): PlaylistItem

    suspend fun followPlaylist(playlist_id: Int, ownerId: Int): PlaylistItem

    suspend fun editPlaylist(playlist_id: Int,title: String, description: String)

    suspend fun addToPlaylist(playlist_id: Int, audioIds: List<Int> )

    suspend fun removeFromPlaylist(playlist_id: Int, audioIds: List<Int>)

    class Base @Inject constructor(
        private val service: PlaylistDataService,
        private val accountDataStore: AccountDataStore,
        private val mapper: AudioIdsToContentIdsMapper
    ): PlaylistDataCloudDataSource {

        override suspend fun createPlaylist(title: String, description: String): PlaylistItem =
            service.createPlaylist(
                accountDataStore.token(),
                accountDataStore.ownerId(),
                title,
                description
            ).response


        override suspend fun followPlaylist(playlist_id: Int, ownerId: Int): PlaylistItem =
            service.followPlaylist(
                accountDataStore.token(),
                ownerId,
                playlist_id
            ).response

        override suspend fun editPlaylist(playlist_id: Int, title: String, description: String) {
            service.editPlaylist(
                accountDataStore.token(),
                accountDataStore.ownerId(),
                playlist_id,
                title,
                description
            )
        }



        override suspend fun addToPlaylist(playlist_id: Int, audioIds: List<Int>) {
            if(audioIds.isEmpty()) return
            service.addToPlaylist(
                accountDataStore.token(),
                accountDataStore.ownerId(),
                playlist_id,
                mapper.map(audioIds)
            )
        }

        override suspend fun removeFromPlaylist(playlist_id: Int, audioIds: List<Int>) {
            if(audioIds.isEmpty()) return
            service.removeFromPlaylist(
                accountDataStore.token(),
                accountDataStore.ownerId(),
                playlist_id,
                mapper.map(audioIds)
            )
        }
    }
}