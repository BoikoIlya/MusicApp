package com.example.musicapp.playlist.data

import com.example.musicapp.app.SpotifyDto.PlaylistDto
import com.example.musicapp.main.data.cache.AccountDataStore
import com.example.musicapp.playlist.data.cache.PlaylistIdTransfer
import com.example.musicapp.playlist.data.cloud.PlaylistService
import javax.inject.Inject

/**
 * Created by HP on 22.05.2023.
 **/
interface PlaylistRepository {

    suspend fun fetchPlaylists(): PlaylistDataDomain

    class Base @Inject constructor(
        private val tokenStore: AccountDataStore,
        private val service: PlaylistService,
        private val mapper: PlaylistDto.Mapper<PlaylistDataDomain>,
        private val transfer: PlaylistIdTransfer
    ): PlaylistRepository {

        override suspend fun fetchPlaylists(): PlaylistDataDomain =
            service.fetchPlaylist(tokenStore.token(),transfer.read()?:"").map(mapper)


    }
}