package com.example.musicapp.favorites.data.cache

import androidx.room.*
import com.example.musicapp.userplaylists.data.cache.PlaylistsAndTracksRelation
import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 20.03.2023.
 **/
@Dao
interface TracksDao {

    companion object{
        const val table_name = "tracks_table"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackCache)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfTracks(track: List<TrackCache>)


    @Query("SELECT * FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId "+
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId " +
            "ORDER BY date DESC")
     fun getAllTracksByTime(query: String, playlistId:Int):Flow<List<TrackCache>>

    @Query("SELECT * FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId "+
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId "+
            "ORDER BY name")
     fun getTracksByName(query: String,playlistId:Int): Flow<List<TrackCache>>

    @Query("SELECT * FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId "+
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId "+
            "ORDER BY artistName")
     fun getTracksByArtist(query: String,playlistId:Int):Flow<List<TrackCache>>

    @Query("SELECT * FROM tracks_table WHERE name = :name AND artistName = :artistName")
    suspend fun contains( name: String, artistName: String): TrackCache?

    @Query("SELECT * FROM tracks_table WHERE trackId = :id")
    suspend fun getById(id: Int): TrackCache?

    @Query("DELETE FROM tracks_table WHERE trackId =:id")
    suspend fun removeTrack(id: Int)

//    @Query("DELETE FROM tracks_table " +
//            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId " +
//            "WHERE trackId NOT IN (:items) AND playlistId = :playlistId")
//    @Query("DELETE FROM tracks_table " +
//            "WHERE trackId IN (SELECT trackId FROM playlists_and_tracks_table WHERE playlistId = :playlistId) " +
//            "AND trackId NOT IN (:items)")
@Query("DELETE FROM tracks_table " +
        "WHERE trackId NOT IN (:items) AND NOT EXISTS " +
        "(SELECT 1 FROM playlists_and_tracks_table WHERE trackId = tracks_table.trackId AND playlistId != :playlistId)")
//@Query("DELETE FROM tracks_table WHERE trackId NOT IN (:items) AND EXISTS (SELECT 1 FROM playlists_and_tracks_table WHERE trackId = tracks_table.trackId AND playlistId = :playlistId) AND NOT EXISTS (SELECT 1 FROM playlists_and_tracks_table WHERE trackId = tracks_table.trackId AND playlistId != :playlistId)")
//@Query("DELETE FROM tracks_table WHERE trackId NOT IN (:items) AND trackId IN () AND trackId NOT IN ()")
    suspend fun deleteTracksNotInListWithSinglePlaylist(items: List<Int>, playlistId: Int)




}