package com.example.musicapp.editplaylist.data.cache

import com.example.musicapp.favorites.data.cache.PlaylistsAndTracksDao
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.userplaylists.data.cache.PlaylistsAndTracksRelation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistTracksCacheDataSource {

    suspend fun savePlaylistTracks(
        playlistId: Int,
        list: List<TrackCache>
    )

    class Base @Inject constructor(
        private val tracksDao: TracksDao,
        private val playlistsAndTracksDao: PlaylistsAndTracksDao
    ): PlaylistTracksCacheDataSource {

        override suspend fun savePlaylistTracks(playlistId: Int,list: List<TrackCache>) {
            coroutineScope {
                launch {
                    tracksDao.insertListOfTracks(list)
                }
                launch {
                    playlistsAndTracksDao.insertRelationsList(list.map {
                        PlaylistsAndTracksRelation(playlistId, it.trackId)
                    })
                }
                launch {
                    tracksDao.deleteTracksNotInListWithSinglePlaylist(list.map { it.trackId }, playlistId)
                }
            }
        }
    }
}