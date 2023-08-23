package com.kamancho.melisma.frienddetails.data

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.ToMediaItemMapper
import com.kamancho.melisma.favorites.data.cache.TracksDao
import com.kamancho.melisma.frienddetails.data.cache.FriendPlaylistsCacheToUiMapper
import com.kamancho.melisma.frienddetails.data.cache.FriendsAndPlaylistsRelationDao
import com.kamancho.melisma.frienddetails.data.cache.FriendsAndTracksRelationsDao
import com.kamancho.melisma.frienddetails.domain.FriendIdAndNameTransfer
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendsDetailsCacheRepository {

   suspend fun searchTracks(query: String): List<MediaItem>

   suspend fun isFriendHaveTracks(): Boolean

   suspend fun isFriendHavePlaylists(): Boolean

    fun searchPlaylists(query: String): List<PlaylistUi>

    class Base @Inject constructor(
        private val transfer: FriendIdAndNameTransfer,
        private val toMediaItemMapper: ToMediaItemMapper,
        private val dao: TracksDao,
        private val friendsAndTracksRelationsDao: FriendsAndTracksRelationsDao,
        private val friendsAndPlaylistsRelationDao: FriendsAndPlaylistsRelationDao,
        private val playlistsDao: PlaylistDao,
        private val toPlaylistUiMapper: FriendPlaylistsCacheToUiMapper
    ): FriendsDetailsCacheRepository {



        override suspend fun searchTracks(query: String): List<MediaItem> {

            return dao.getTracksOfFriend(
                    query,
                    transfer.read()!!.first.toInt()
                )
                .map { toMediaItemMapper.map(Pair(it, emptyList())) }
        }

        override suspend fun isFriendHaveTracks(): Boolean {
            return friendsAndTracksRelationsDao.friendTrackCount(transfer.read()!!.first.toInt())!=0
        }

        override suspend fun isFriendHavePlaylists(): Boolean {
            return friendsAndPlaylistsRelationDao.friendPlaylistCount(transfer.read()!!.first.toInt()) != 0
        }


        override fun searchPlaylists(query: String): List<PlaylistUi> {
            return toPlaylistUiMapper.map(playlistsDao.getPlaylistsOfFriend(query, transfer.read()!!.first.toInt()))
        }
    }
}