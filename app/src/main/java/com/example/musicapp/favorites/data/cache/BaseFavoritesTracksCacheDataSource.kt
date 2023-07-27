package com.example.musicapp.favorites.data.cache

import com.example.musicapp.app.core.FavoritesCacheDataSource
import com.example.musicapp.main.di.AppModule.Companion.mainPlaylistId
import com.example.musicapp.userplaylists.data.cache.PlaylistsAndTracksRelation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 08.07.2023.
 **/


class BaseFavoritesTracksCacheDataSource @Inject constructor(
    private val tracksDao: TracksDao,
    private val playlistsAndTracksRelationsDao: PlaylistsAndTracksDao
    ): FavoritesCacheDataSource<TrackCache> {

        override suspend fun contains(data: Pair<String, String>): Boolean
        = tracksDao.contains(data.first, data.second) != null

        override suspend fun insertWithNewId(newId: Int, data: TrackCache) {
            tracksDao.insertTrack(data.copy(trackId = newId))
            playlistsAndTracksRelationsDao.insertRelationDao(PlaylistsAndTracksRelation(mainPlaylistId,newId))
        }

        override suspend fun insert(data: TrackCache) {
            tracksDao.insertTrack(data)
            playlistsAndTracksRelationsDao.insertRelationDao(PlaylistsAndTracksRelation(mainPlaylistId,data.trackId))
        }

        override suspend fun removeFromDB(id: Int) {
            playlistsAndTracksRelationsDao.deleteOneRelation(id, mainPlaylistId)
            tracksDao.removeTrack(id)
        }

        override suspend fun getById(id: Int): TrackCache = tracksDao.getById(id)?: throw NoSuchElementException()

        override suspend fun update(list: List<TrackCache>) {
            val trackIds = list.map { it.trackId}
            coroutineScope {
                launch { tracksDao.insertListOfTracks(list) }
                launch{ tracksDao.deleteTracksNotInListWithSinglePlaylist(trackIds,mainPlaylistId)}
                launch { playlistsAndTracksRelationsDao.insertRelationsList(trackIds.map { PlaylistsAndTracksRelation(mainPlaylistId,it) }) }
                launch { playlistsAndTracksRelationsDao.deleteRelationsOfTrackIdsNotInList(trackIds, mainPlaylistId) }
            }
        }

    }
