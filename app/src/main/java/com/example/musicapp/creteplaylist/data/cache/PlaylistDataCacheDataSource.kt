package com.example.musicapp.creteplaylist.data.cache

import com.example.musicapp.favorites.data.cache.PlaylistsAndTracksDao
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.data.cache.PlaylistsAndTracksRelation
import java.time.Instant
import javax.inject.Inject

/**
 * Created by HP on 17.07.2023.
 **/
interface PlaylistDataCacheDataSource {

    suspend fun addTracksToPlaylist(playlistId: Int, audioIds: List<Int>)

    suspend fun removeTracksFromPlaylist(playlistId: Int, audioIds: List<Int>)

    suspend fun insertPlaylist(data: PlaylistCache)


    suspend fun updatePlaylist(
        playlist_id: Int,
        title: String,
        description: String,
    )

    class Base @Inject constructor(
        private val playlistDao: PlaylistDao,
        private val playlistsAndTracksDao: PlaylistsAndTracksDao
    ): PlaylistDataCacheDataSource{

        override suspend fun addTracksToPlaylist(playlistId: Int, audioIds: List<Int>) {
            if(audioIds.isEmpty()) return
            playlistsAndTracksDao.insertRelationsList(
                audioIds.map { PlaylistsAndTracksRelation(playlistId,it) }
            )
        }

        override suspend fun removeTracksFromPlaylist(playlistId: Int, audioIds: List<Int>) {
            playlistsAndTracksDao.deleteRelationInSignificantPlaylistThatContainsInIdsList(playlistId,audioIds)
        }

        override suspend fun insertPlaylist(data: PlaylistCache) {
            playlistDao.insertPlaylist(data)
        }

        override suspend fun updatePlaylist(playlist_id: Int, title: String, description: String) {
            val cached = playlistDao.getPlaylistById(playlist_id)
            playlistDao.insertPlaylist(cached!!.copy(
                title = title,
                description = description,
            ))
        }


    }
}