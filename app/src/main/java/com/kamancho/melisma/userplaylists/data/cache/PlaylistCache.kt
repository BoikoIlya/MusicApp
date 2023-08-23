package com.kamancho.melisma.userplaylists.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao.Companion.playlists_table

/**
 * Created by HP on 12.07.2023.
 **/
@Entity(tableName = playlists_table)
data class PlaylistCache(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("playlistId")
    val playlistId: String,
    val title: String,
    val is_following: Boolean,
    val count: Int,
    val update_time: Int,
    val description: String,
    val owner_id: Int,
    @TypeConverters(PlaylistThumbsConverters::class)
    val thumbs: List<String>
)
