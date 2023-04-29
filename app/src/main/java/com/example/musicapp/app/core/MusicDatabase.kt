package com.example.musicapp.app.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.searchhistory.data.cache.HistoryDao
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache

/**
 * Created by HP on 20.03.2023.
 **/
@Database(entities = [TrackCache::class, HistoryItemCache::class], version = 1)
abstract class MusicDatabase: RoomDatabase() {

    abstract fun getTracksDao(): TracksDao

    abstract fun getHistoryDao(): HistoryDao
}