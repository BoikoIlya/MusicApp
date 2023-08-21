package com.example.musicapp.favorites.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

/**
 * Created by HP on 20.03.2023.
 **/
@Entity(tableName = TracksDao.table_name )
data class TrackCache(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("trackId")
    val trackId: String,
    val url: String,
    val name: String,
    val artistName: String,
    val bigImgUrl: String,
    val smallImgUrl: String,
    val albumName: String,
    val date: Int,
    val durationFormatted: String,
    val durationInMillis: Float,
    val ownerId: Int,
   // val isCached: Boolean = false
)
