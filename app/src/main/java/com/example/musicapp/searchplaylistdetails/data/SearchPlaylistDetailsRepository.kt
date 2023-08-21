package com.example.musicapp.searchplaylistdetails.data

import android.util.Log
import com.example.musicapp.app.vkdto.SearchPlaylistItem
import com.example.musicapp.editplaylist.data.cloud.PlaylistDetailsCloudDataSource
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cloud.TracksCloudToCacheMapper
import com.example.musicapp.searchplaylistdetails.data.cache.SearchPlaylistTracksCacheDataSource
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
            Log.d("tag", "contains: $title")//...
            val result = playlistDao.contains(title)
            Log.d("tag", "contains: result $result")
           return result!=null
        }
    }
}