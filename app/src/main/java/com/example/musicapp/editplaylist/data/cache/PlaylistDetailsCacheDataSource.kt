package com.example.musicapp.editplaylist.data.cache

import android.util.Log
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.favorites.data.cache.PlaylistsAndTracksDao
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.data.cache.PlaylistsAndTracksRelation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistDetailsCacheDataSource {

    suspend fun savePlaylistTracks(
        playlistId: String,
        list: List<TrackCache>
    )

    suspend fun updatePlaylistData(playlistId: String,item: PlaylistCache)

   

    abstract class Abstract (
        private val tracksDao: TracksDao,
        private val playlistsAndTracksDao: PlaylistsAndTracksDao,
        private val dispatchersList: DispatchersList
    ): PlaylistDetailsCacheDataSource {

        override suspend fun savePlaylistTracks(playlistId: String,list: List<TrackCache>) {
            val trackIds =list.map { it.trackId }
            coroutineScope {
//                launch { tracksDao.insertListOfTracks(list) }
//                launch {
//                    playlistsAndTracksDao.insertRelationsList(list.map {
//                        PlaylistsAndTracksRelation(playlistId, it.trackId)
//                    })
//                }
//                launch { tracksDao.deleteTracksNotInListWithSinglePlaylist(trackIds, playlistId) }
                launch(dispatchersList.io()){
                    playlistsAndTracksDao.clearAndInsertRelations(
                    playlistId,
                    list.map { PlaylistsAndTracksRelation(playlistId, it.trackId)} )
                }
                launch(dispatchersList.io()) {
                    tracksDao.insertAndDeleteTracksWithSinglePlaylistTransaction(list,playlistId)
                }
            }
        }

        override suspend fun updatePlaylistData(playlistId: String, item: PlaylistCache) = Unit
        
        
    }
    
    interface PlaylistDetailsCacheDataSourceFoundById: PlaylistDetailsCacheDataSource{

        suspend fun getPlaylistById(playlistId: String): PlaylistCache
    }

    class Base @Inject constructor (
        tracksDao: TracksDao,
        playlistsAndTracksDao: PlaylistsAndTracksDao,
        private val playlistsDao: PlaylistDao,
        dispatchersList: DispatchersList
    ): Abstract(tracksDao, playlistsAndTracksDao,dispatchersList),PlaylistDetailsCacheDataSourceFoundById {

        override suspend fun updatePlaylistData(playlistId: String, item: PlaylistCache) {
            playlistsDao.insertPlaylist(item)
        }

        override suspend fun getPlaylistById(playlistId: String): PlaylistCache =
            playlistsDao.getPlaylistById(playlistId).first() ?: throw NoSuchElementException()
    }
}