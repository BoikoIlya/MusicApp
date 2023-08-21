package com.example.musicapp.userplaylists.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.musicapp.favorites.data.cache.PlaylistsAndTracksDao.Companion.playlists_and_tracks_table
import com.example.musicapp.favorites.data.cache.TrackCache

/**
 * Created by HP on 12.07.2023.
 **/
@Entity(
    primaryKeys = ["playlistId","trackId"],
    tableName = playlists_and_tracks_table,
    )
data class PlaylistsAndTracksRelation(
    @ColumnInfo("playlistId")
    val playlistId: String,
    @ColumnInfo("trackId")
    val trackId: String
)
