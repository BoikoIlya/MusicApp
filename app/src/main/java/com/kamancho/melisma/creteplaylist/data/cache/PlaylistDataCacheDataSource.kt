package com.kamancho.melisma.creteplaylist.data.cache

import com.kamancho.melisma.favorites.data.cache.PlaylistsAndTracksDao
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import com.kamancho.melisma.userplaylists.data.cache.PlaylistsAndTracksRelation
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by HP on 17.07.2023.
 **/
interface PlaylistDataCacheDataSource {

    suspend fun addTracksToPlaylist(playlistId: String, audioIds: List<Int>)

    suspend fun removeTracksFromPlaylist(playlistId: String, audioIds: List<Int>)

    suspend fun insertPlaylist(data: PlaylistCache)

    suspend fun updateTracksCountInPlaylist(playlistId: String)

    suspend fun updatePlaylistThumbs(playlistId: String)

    suspend fun checkIfTitleOrDescriptionDifferent(
        title: String,
        description: String,
        playlistId: String
    ):Boolean

    suspend fun updatePlaylist(
        playlist_id: String,
        title: String,
        description: String,
    )

    abstract class Abstract(
        private val playlistDao: PlaylistDao,
        private val playlistsAndTracksDao: PlaylistsAndTracksDao
    ):PlaylistDataCacheDataSource{

        override suspend fun updateTracksCountInPlaylist(playlistId: String) {
            val playlist = playlistDao.getPlaylistById(playlistId).first()
            val count = playlistsAndTracksDao.countTracks(playlistId)
            playlistDao.insertPlaylist(playlist?.copy(count = count)?:throw NoSuchElementException())
        }

        override suspend fun updatePlaylistThumbs(playlistId: String) {
            val images = playlistDao.selectFourFirstImagesFromPlaylistTracks(playlistId)
            playlistDao.insertPlaylist(
                playlistDao.getPlaylistById(playlistId).first()?.copy(thumbs = images)
                    ?:throw NoSuchElementException()
            )
        }


    }

    class Base @Inject constructor(
        private val playlistDao: PlaylistDao,
        private val playlistsAndTracksDao: PlaylistsAndTracksDao
    ): PlaylistDataCacheDataSource, Abstract(playlistDao, playlistsAndTracksDao){

        override suspend fun addTracksToPlaylist(playlistId: String, audioIds: List<Int>) {
            if(audioIds.isEmpty()) return
            playlistsAndTracksDao.insertRelationsList(
                audioIds.map { PlaylistsAndTracksRelation(playlistId,it.toString()) }
            )
            super.updateTracksCountInPlaylist(playlistId)
            super.updatePlaylistThumbs(playlistId)
        }

        override suspend fun removeTracksFromPlaylist(playlistId: String, audioIds: List<Int>) {
            playlistsAndTracksDao.deleteRelationInSignificantPlaylistThatContainsInIdsList(playlistId,audioIds)
            super.updateTracksCountInPlaylist(playlistId)
            super.updatePlaylistThumbs(playlistId)
        }

        override suspend fun insertPlaylist(data: PlaylistCache) {
            playlistDao.insertPlaylist(data)
        }



        override suspend fun checkIfTitleOrDescriptionDifferent(
            title: String,
            description: String,
            playlistId: String
        ): Boolean {
            val item = playlistDao.getPlaylistById(playlistId).first()?:throw NoSuchElementException()
            return item.title!=title || item.description!=description
        }

        override suspend fun updatePlaylist(playlist_id: String, title: String, description: String) {
            val cached = playlistDao.getPlaylistById(playlist_id)
            playlistDao.insertPlaylist(cached.first()?.copy(
                title = title,
                description = description,
            ) ?: throw NoSuchElementException())
        }


    }
}