package com.example.musicapp.editplaylist.domain

import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.editplaylist.data.PlaylistTracksRepository
import com.example.musicapp.editplaylist.data.cache.PlaylistTracksCacheDataSource
import com.example.musicapp.editplaylist.data.cloud.PlaylistTracksCloudDataSource
import com.example.musicapp.favorites.data.cloud.TracksCloudToCacheMapper
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistTracksInteractor {

    suspend fun fetch(playlistId: Int): PlaylistsResult

    class Base @Inject constructor(
        private val repository: PlaylistTracksRepository,
        private val handleResponse: HandleResponse
    ): PlaylistTracksInteractor {

        override suspend fun fetch(playlistId: Int): PlaylistsResult = handleResponse.handle({
            repository.fetch(playlistId)
            PlaylistsResult.Success("")
        },{message,error->
            PlaylistsResult.Error(message)
        })
    }
}