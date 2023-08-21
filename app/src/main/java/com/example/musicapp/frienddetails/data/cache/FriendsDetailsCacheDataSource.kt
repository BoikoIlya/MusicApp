package com.example.musicapp.frienddetails.data.cache

import android.util.Log
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.MusicDatabase
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendsDetailsCacheDataSource {

    suspend fun insertTracks(list: List<TrackCache>, friendId: String)

    suspend fun insertPlaylists(list: List<PlaylistCache>, friendId: String)

    class Base @Inject constructor(
        private val tracksDao: TracksDao,
        private val playlistsDao: PlaylistDao,
        private val friendsAndTracksRelationsDao: FriendsAndTracksRelationsDao,
        private val friendsAndPlaylistsRelationDao: FriendsAndPlaylistsRelationDao,
        private val dispatchersList: DispatchersList
    ): FriendsDetailsCacheDataSource {

        override suspend fun insertTracks(list: List<TrackCache>, friendId: String) {
            coroutineScope {

               launch(dispatchersList.io()) {
                   tracksDao.deleteTracksOfFriendNotInList(friendId.toInt())
                    tracksDao.insertListOfTracks(list)
                }
                launch(dispatchersList.io()) {
                    friendsAndTracksRelationsDao.deleteRelationsNotInList(friendId.toInt())
                    friendsAndTracksRelationsDao.insertRelationsList(
                        list.map { FriendAndTracksRelation(it.trackId, friendId.toInt()) }
                    )
                }

                }



        }

        override suspend fun insertPlaylists(list: List<PlaylistCache>, friendId: String) {

        coroutineScope {
            launch(dispatchersList.io()) {
                playlistsDao.insertListOfPlaylists(list)
            }
            launch(dispatchersList.io()) {
                friendsAndPlaylistsRelationDao.insertList(
                    list.map { FriendAndPlaylistRelation(it.playlistId, friendId.toInt()) }
                )
            }

            val idsList = list.map { it.playlistId }
            launch(dispatchersList.io()) {
                playlistsDao.deletePlaylistsOfFriendNotInList(idsList, friendId.toInt())
            }
            launch(dispatchersList.io()) {
                friendsAndPlaylistsRelationDao.deleteRelationsNotInList(idsList)
            }
            }
        }


    }
}