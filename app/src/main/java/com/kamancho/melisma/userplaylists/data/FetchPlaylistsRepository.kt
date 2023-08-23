package com.kamancho.melisma.userplaylists.data

import com.kamancho.melisma.favorites.data.cache.PlaylistsCacheToDomainMapper
import com.kamancho.melisma.main.di.AppModule
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by HP on 20.07.2023.
 **/
interface FetchPlaylistsRepository {

    fun fetchAll(query: String): Flow<List<PlaylistDomain>>

    fun fetchUserCreated(query: String): Flow<List<PlaylistDomain>>

    fun getPlaylistById(id: String): Flow<PlaylistDomain>

    class Base @Inject constructor(
        private val dao: PlaylistDao,
        private val playlistsCacheToDomainMapper: PlaylistsCacheToDomainMapper
    ): FetchPlaylistsRepository {

        override fun fetchAll(query: String): Flow<List<PlaylistDomain>> =
            dao.getPlaylistsOrderByUpdateTime(query, AppModule.mainPlaylistId.toString()).map { playlistsCacheToDomainMapper.map(it)  }


        override fun fetchUserCreated(query: String): Flow<List<PlaylistDomain>> =
            dao.getPlaylistsFollowedOrNotOrderByUpdateTime(
                query,
                AppModule.mainPlaylistId.toString(),
                false
                ).map { playlistsCacheToDomainMapper.map(it)  }

        override fun getPlaylistById(id: String): Flow<PlaylistDomain> =
            dao.getPlaylistById(id.toString()).map {
                if(it!=null)
                playlistsCacheToDomainMapper.map(listOf(it)).first()
                else PlaylistDomain(
                    playlistId = "0",
                    title = "",
                    isFollowing = false,
                    count = 0,
                    description = "",
                    ownerId = 0,
                    thumbs = listOf()
                )
            }
    }
}