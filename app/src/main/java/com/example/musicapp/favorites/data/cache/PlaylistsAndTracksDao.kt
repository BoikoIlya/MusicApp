package com.example.musicapp.favorites.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicapp.userplaylists.data.cache.PlaylistsAndTracksRelation

/**
 * Created by HP on 12.07.2023.
 **/
@Dao
interface PlaylistsAndTracksDao {

    companion object{
        const val playlists_and_tracks_table = "playlists_and_tracks_table"
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationsList(list: List<PlaylistsAndTracksRelation>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelationDao(item: PlaylistsAndTracksRelation)

    @Query("DELETE FROM playlists_and_tracks_table " +
            "WHERE trackId NOT IN (:trackIds) AND playlistId =:playlistId")
    suspend fun deleteRelationsOfTrackIdsNotInList(trackIds: List<Int>, playlistId: Int)

    @Query("DELETE FROM playlists_and_tracks_table "+
            "WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun deleteOneRelation(trackId: Int, playlistId: Int)

    @Query("DELETE FROM playlists_and_tracks_table "+
            "WHERE  playlistId = :playlistId")
    suspend fun clearRelationsOfOnePlaylist(playlistId: Int)

    @Query("DELETE FROM playlists_and_tracks_table "+
            "WHERE playlistId NOT IN (:listIds) AND playlistId != :mainPlaylistId")
    suspend fun deleteRelationsNotContainsInListOfPlaylistsIds(listIds: List<Int>,mainPlaylistId: Int)

    @Query("DELETE FROM playlists_and_tracks_table " +
            "WHERE playlistId =:playlistId AND trackId IN (:trackIds)")
    suspend fun deleteRelationInSignificantPlaylistThatContainsInIdsList(playlistId: Int, trackIds: List<Int>)

}