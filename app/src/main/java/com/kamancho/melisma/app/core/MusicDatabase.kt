package com.kamancho.melisma.app.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kamancho.melisma.favorites.data.cache.PlaylistsAndTracksDao
import com.kamancho.melisma.favorites.data.cache.TracksDao
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.frienddetails.data.cache.FriendAndPlaylistRelation
import com.kamancho.melisma.frienddetails.data.cache.FriendAndTracksRelation
import com.kamancho.melisma.frienddetails.data.cache.FriendsAndPlaylistsRelationDao
import com.kamancho.melisma.frienddetails.data.cache.FriendsAndTracksRelationsDao
import com.kamancho.melisma.friends.data.cache.FriendCache
import com.kamancho.melisma.friends.data.cache.FriendsDao
import com.kamancho.melisma.searchhistory.data.cache.HistoryDao
import com.kamancho.melisma.searchhistory.data.cache.HistoryItemCache
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import com.kamancho.melisma.userplaylists.data.cache.PlaylistThumbsConverters
import com.kamancho.melisma.userplaylists.data.cache.PlaylistsAndTracksRelation

/**
 * Created by HP on 20.03.2023.
 **/
@Database(
    entities = [
        TrackCache::class,
        HistoryItemCache::class,
        PlaylistCache::class,
        PlaylistsAndTracksRelation::class,
        FriendCache::class,
        FriendAndTracksRelation::class,
        FriendAndPlaylistRelation::class
               ],
    version = 1,
    exportSchema = true,
    )
@TypeConverters(PlaylistThumbsConverters::class)
abstract class MusicDatabase: RoomDatabase() {

    abstract fun getTracksDao(): TracksDao
    abstract fun getHistoryDao(): HistoryDao
    abstract fun getPlaylistsAndTracksDao(): PlaylistsAndTracksDao
    abstract fun getPlaylistDao(): PlaylistDao
    abstract fun getFriendsDao(): FriendsDao
    abstract fun getFriendsAndTracksRelationDao(): FriendsAndTracksRelationsDao
    abstract fun getFriendsAndPlaylistsRelationDao(): FriendsAndPlaylistsRelationDao
}