package com.kamancho.melisma.userplaylists.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.kamancho.melisma.favorites.data.cache.PlaylistsAndTracksDao.Companion.playlists_and_tracks_table

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
