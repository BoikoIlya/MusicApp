package com.example.musicapp.frienddetails.data

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.ToMediaItemMapper
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.frienddetails.data.cache.FriendPlaylistsCacheToUiMapper
import com.example.musicapp.frienddetails.data.cache.FriendsAndPlaylistsRelationDao
import com.example.musicapp.frienddetails.data.cache.FriendsAndTracksRelationsDao
import com.example.musicapp.frienddetails.domain.FriendIdAndNameTransfer
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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

        companion object{
            const val tracks_chunk_size = 2000
        }

        override suspend fun searchTracks(query: String): List<MediaItem> {

            return dao.getTracksOfFriend(
                    query,
                    transfer.read()!!.first.toInt()
                )
                //.map { trackList ->
                      //  trackList
                            .map { toMediaItemMapper.map(it) }
                  //  }
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