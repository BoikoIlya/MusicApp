package com.kamancho.melisma.searchplaylistdetails.data

import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.editplaylist.data.cloud.PlaylistDetailsCloudDataSource
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.favorites.data.cloud.TracksCloudToCacheMapper
import com.kamancho.melisma.main.di.AppModule
import com.kamancho.melisma.searchplaylistdetails.data.cache.SearchPlaylistTracksCacheDataSource
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by HP on 15.08.2023.
 **/
interface SearchPlaylistDetailsRepository {

    suspend fun fetch(playlistId: String,ownerId: Int): Pair<SearchPlaylistItem,List<TrackCache>>

    suspend fun find(query: String): List<TrackCache>

    suspend fun contains(title: String): Boolean

    class Base @Inject constructor(
        private val cache: SearchPlaylistTracksCacheDataSource,
        private val cloud: PlaylistDetailsCloudDataSource,
        private val tracksCloudToCacheMapper: TracksCloudToCacheMapper,
        private val playlistDao: PlaylistDao
    ): SearchPlaylistDetailsRepository {

        override suspend fun fetch(playlistId: String, ownerId: Int): Pair<SearchPlaylistItem,List<TrackCache>> =
            coroutineScope {
               val playlistDetailsDeferred = async {
                   cloud.fetchSearchPlaylistById(playlistId,ownerId)
                }
                val tracksDeferred = async {
                    val result = cloud.fetchTracks(playlistId, ownerId)
                    cache.write(tracksCloudToCacheMapper.map(result))
                }

                tracksDeferred.await()
                val playlistItem = playlistDetailsDeferred.await()

                return@coroutineScope Pair(playlistItem, cache.read())
            }


        override suspend fun find(query: String): List<TrackCache> {
            val result = cache.read().filter {
                it.name.contains(query,true) || it.artistName.contains(query,true)
            }
            return  if(query.isEmpty() && result.isEmpty())  cache.read()
                    else result
        }

        override suspend fun contains(title: String): Boolean {
           return playlistDao.getPlaylistsOrderByUpdateTime(title, AppModule.mainPlaylistId.toString()).first().isNotEmpty()
        }
    }
}