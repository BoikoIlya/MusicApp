package com.kamancho.melisma.frienddetails.data

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.ToMediaItemMapper
import com.kamancho.melisma.favorites.data.cache.TracksDao
import com.kamancho.melisma.frienddetails.data.cache.FriendPlaylistsCacheToUiMapper
import com.kamancho.melisma.frienddetails.data.cache.FriendsAndPlaylistsRelationDao
import com.kamancho.melisma.frienddetails.data.cache.FriendsAndTracksRelationsDao
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendsDetailsCacheRepository {

   suspend fun searchTracks(query: String,id: String): List<MediaItem>

   suspend fun isFriendHaveTracks(id: String): Boolean

   suspend fun isFriendHavePlaylists(id: String): Boolean

    fun searchPlaylists(query: String,id: String): List<PlaylistUi>

    class Base @Inject constructor(
        private val toMediaItemMapper: ToMediaItemMapper,
        private val dao: TracksDao,
        private val friendsAndTracksRelationsDao: FriendsAndTracksRelationsDao,
        private val friendsAndPlaylistsRelationDao: FriendsAndPlaylistsRelationDao,
        private val playlistsDao: PlaylistDao,
        private val toPlaylistUiMapper: FriendPlaylistsCacheToUiMapper
    ): FriendsDetailsCacheRepository {



        override suspend fun searchTracks(query: String,id: String): List<MediaItem> {

            return dao.getTracksOfFriend(
                    query,
                    id.toInt()
                )
                .map { toMediaItemMapper.map(Pair(it, emptyMap())) }
        }

        override suspend fun isFriendHaveTracks(id: String): Boolean {
            return friendsAndTracksRelationsDao.friendTrackCount(id.toInt())!=0
        }

        override suspend fun isFriendHavePlaylists(id: String): Boolean {
            return friendsAndPlaylistsRelationDao.friendPlaylistCount(id.toInt()) != 0
        }


        override fun searchPlaylists(query: String,id: String): List<PlaylistUi> {
            return toPlaylistUiMapper.map(playlistsDao.getPlaylistsOfFriend(query, id.toInt()))
        }
    }
}