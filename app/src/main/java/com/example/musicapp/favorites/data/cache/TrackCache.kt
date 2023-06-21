package com.example.musicapp.favorites.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by HP on 20.03.2023.
 **/
@Entity(tableName = TracksDao.table_name )
data class TrackCache(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val url: String,
    val name: String,
    val artistName: String,
    val bigImgUrl: String,
    val smallImgUrl: String,
    val albumName: String,
    val date: Int
)
