package com.example.musicapp.userplaylists.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.musicapp.userplaylists.data.cache.PlaylistDao.Companion.playlists_table

/**
 * Created by HP on 12.07.2023.
 **/
@Entity(tableName = playlists_table)
data class PlaylistCache(
    @PrimaryKey(autoGenerate = false)
    val playlistId: Int,
    val title: String,
    val is_following: Boolean,
    val count: Int,
    val create_time: Int,
    val description: String,
    val owner_id: Int,
    @TypeConverters(PlaylistThumbsConverters::class)
    val thumbs: List<String>
)
