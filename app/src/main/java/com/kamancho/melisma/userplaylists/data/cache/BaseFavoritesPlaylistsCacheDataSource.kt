package com.kamancho.melisma.userplaylists.data.cache

import com.kamancho.melisma.app.core.FavoritesCacheDataSource
import com.kamancho.melisma.favorites.data.cache.PlaylistsAndTracksDao
import com.kamancho.melisma.main.di.AppModule.Companion.mainPlaylistId
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 12.07.2023.
 **/
class BaseFavoritesPlaylistsCacheDataSource @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val playlistsAndTracksDao: PlaylistsAndTracksDao,
): FavoritesCacheDataSource<PlaylistCache> {

    override suspend fun contains(data: Pair<String, String>): Boolean {
       return playlistDao.getPlaylistsOrderByUpdateTime(data.first, mainPlaylistId.toString()).first().isNotEmpty()
    }

    override suspend fun removeFromDB(id: Int) {
        playlistDao.deleteTracksWhichAssociatedOnlyWithCurrentPlaylist(id.toString())
        playlistsAndTracksDao.clearRelationsOfOnePlaylist(id.toString())
        playlistDao.deletePlaylist(id.toString())
    }

    override suspend fun getById(id: Int): PlaylistCache = playlistDao.getPlaylistById(id.toString()).first()?:throw NoSuchElementException()

    override suspend fun update(list: List<PlaylistCache>) {
        val playlistsIds = list.map { it.playlistId }
        coroutineScope{
            launch{ playlistDao.insertListOfPlaylists(list) }
            launch { playlistsAndTracksDao.insertRelationsList(playlistsIds.map { PlaylistsAndTracksRelation(it, mainPlaylistId.toString()) }) }
            launch { playlistDao.deletePlaylistsNotInList(playlistsIds, mainPlaylistId.toString()) }
            launch { playlistsAndTracksDao.deleteRelationsNotContainsInListOfPlaylistsIds(playlistsIds, mainPlaylistId) }
        }
    }

    override suspend fun insert(data: PlaylistCache) {
        playlistDao.insertPlaylist(data)
    }

    override suspend fun insertWithNewId(newId: Int, data: PlaylistCache) {
        playlistDao.insertPlaylist(data.copy(playlistId = newId.toString()))
    }
}