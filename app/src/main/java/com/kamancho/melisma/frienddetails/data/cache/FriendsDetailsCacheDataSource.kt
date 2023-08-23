package com.kamancho.melisma.frienddetails.data.cache

import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.favorites.data.cache.TracksDao
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import kotlinx.coroutines.coroutineScope
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
            val idsList = list.map { it.playlistId }
            launch(dispatchersList.io()) {
                playlistsDao.clearAndAddFriendPlaylists(list,friendId.toInt())
            }
            launch(dispatchersList.io()) {
                friendsAndPlaylistsRelationDao.clearAndInsertRelations(
                    list.map { FriendAndPlaylistRelation(it.playlistId, friendId.toInt()) },
                    friendId.toInt()
                )
            }

            }
        }


    }
}