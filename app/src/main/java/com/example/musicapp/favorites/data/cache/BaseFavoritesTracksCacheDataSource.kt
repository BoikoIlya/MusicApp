package com.example.musicapp.favorites.data.cache

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.musicapp.app.core.FavoritesCacheDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 08.07.2023.
 **/


class BaseFavoritesTracksCacheDataSource @Inject constructor(
        private val dao: TracksDao
    ): FavoritesCacheDataSource<TrackCache> {

        override suspend fun contains(nameAndAuthor: Pair<String, String>): Boolean
        = dao.contains(nameAndAuthor.first, nameAndAuthor.second) != null

        override suspend fun insertWithNewId(id: Int, data: TrackCache) =
            dao.insertTrack(data.copy(id = id))

        override suspend fun insert(trackCache: TrackCache) = dao.insertTrack(trackCache)

        override suspend fun removeFromDB(id: Int) {
            dao.removeTrack(id)
        }

        override suspend fun getById(id: Int): TrackCache = dao.getById(id)?: throw NoSuchElementException()

        override suspend fun update(list: List<TrackCache>) {
            coroutineScope {
                launch { dao.insertListOfTracks(list) }
                launch{ dao.deleteItemsNotInList(list.map { it.id})}
            }
        }

    }
