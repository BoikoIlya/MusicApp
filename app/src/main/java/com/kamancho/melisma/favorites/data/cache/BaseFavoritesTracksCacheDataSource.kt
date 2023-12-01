package com.kamancho.melisma.favorites.data.cache

import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.FavoritesCacheDataSource
import com.kamancho.melisma.editplaylist.data.cache.PlaylistDetailsCacheDataSource
import com.kamancho.melisma.main.di.AppModule.Companion.mainPlaylistId
import com.kamancho.melisma.userplaylists.data.cache.PlaylistsAndTracksRelation
import javax.inject.Inject

/**
 * Created by HP on 08.07.2023.
 **/


class BaseFavoritesTracksCacheDataSource @Inject constructor(
    private val tracksDao: TracksDao,
    private val playlistsAndTracksRelationsDao: PlaylistsAndTracksDao,
    dispatchersList: DispatchersList
    ): FavoritesCacheDataSource<TrackCache>,
    PlaylistDetailsCacheDataSource.Abstract(tracksDao,playlistsAndTracksRelationsDao,dispatchersList) {



        override suspend fun contains(data: Pair<String, String>): Boolean =
        tracksDao.contains(data.first, data.second, mainPlaylistId) != null


    override suspend fun insertWithNewId(newId: Int, data: TrackCache) {
            tracksDao.insertTrack(data.copy(trackId = newId.toString()))
            playlistsAndTracksRelationsDao.insertRelationDao(PlaylistsAndTracksRelation(mainPlaylistId.toString(),newId.toString()))
        }

        override suspend fun insert(data: TrackCache) {
            tracksDao.insertTrack(data)
            playlistsAndTracksRelationsDao.insertRelationDao(PlaylistsAndTracksRelation(mainPlaylistId.toString(),data.trackId))
        }

        override suspend fun removeFromDB(id: Int) {
            playlistsAndTracksRelationsDao.deleteOneRelation(id, mainPlaylistId.toString())
            tracksDao.removeTrack(id)
        }

        override suspend fun getById(id: Int): TrackCache = tracksDao.getById(id)?: throw NoSuchElementException()

        override suspend fun update(list: List<TrackCache>) = savePlaylistTracks(mainPlaylistId.toString(),list)

    }