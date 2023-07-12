package com.example.musicapp.favorites.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.example.musicapp.favorites.data.cache.TracksDao.Companion.fts_table_name

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
    val date: Int,
    val durationFormatted: String,
    val durationInMillis: Float,
    val ownerId: Int,
    val playlistId: Int = currentAccountAllMusicPlaylistId
){

    companion object{
        const val currentAccountAllMusicPlaylistId = -1
    }

   suspend fun containsInDb(dao: TracksDao) = dao.contains(name,artistName)

}
