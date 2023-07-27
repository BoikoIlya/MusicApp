package com.example.musicapp.app.core

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.musicapp.favorites.data.cache.PlaylistsAndTracksDao
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.searchhistory.data.cache.HistoryDao
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.data.cache.PlaylistThumbsConverters
import com.example.musicapp.userplaylists.data.cache.PlaylistsAndTracksRelation

/**
 * Created by HP on 20.03.2023.
 **/
@Database(
    entities = [
        TrackCache::class,
        HistoryItemCache::class,
        PlaylistCache::class,
        PlaylistsAndTracksRelation::class],
    version = 1,
    exportSchema = true,
    )
@TypeConverters(PlaylistThumbsConverters::class)
abstract class MusicDatabase: RoomDatabase() {

    abstract fun getTracksDao(): TracksDao
    abstract fun getHistoryDao(): HistoryDao
    abstract fun getPlaylistsAndTracksDao(): PlaylistsAndTracksDao
    abstract fun getPlaylistDao(): PlaylistDao


}