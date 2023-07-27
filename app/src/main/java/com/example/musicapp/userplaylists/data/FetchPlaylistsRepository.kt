package com.example.musicapp.userplaylists.data

import com.example.musicapp.favorites.data.cache.PlaylistsCacheToDomainMapper
import com.example.musicapp.main.di.AppModule
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by HP on 20.07.2023.
 **/
interface FetchPlaylistsRepository {

    fun fetchAll(query: String): Flow<List<PlaylistDomain>>

    fun fetchUserCreated(query: String): Flow<List<PlaylistDomain>>

    class Base @Inject constructor(
        private val dao: PlaylistDao,
        private val playlistsCacheToDomainMapper: PlaylistsCacheToDomainMapper
    ): FetchPlaylistsRepository {

        override fun fetchAll(query: String): Flow<List<PlaylistDomain>> =
            dao.getPlaylistsOrderByCreateTime(query, AppModule.mainPlaylistId).map { playlistsCacheToDomainMapper.map(it)  }


        override fun fetchUserCreated(query: String): Flow<List<PlaylistDomain>> =
            dao.getPlaylistsFollowedOrNotOrderByCreateTime(
                query,
                AppModule.mainPlaylistId,
                false
                ).map { playlistsCacheToDomainMapper.map(it)  }
    }
}