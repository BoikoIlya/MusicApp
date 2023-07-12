package com.example.musicapp.favorites.data.cache

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 20.03.2023.
 **/
@Dao
interface TracksDao {

    companion object{
        const val table_name = "tracks_table"
        const val fts_table_name = "fts_tracks_table"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackCache)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfTracks(track: List<TrackCache>)


    @Query("SELECT * FROM tracks_table " +
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId " +
            "ORDER BY date DESC")
     fun getAllTracksByTime(query: String, playlistId:Int):Flow<List<TrackCache>>

    @Query("SELECT * FROM tracks_table " +
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId "+
            "ORDER BY name")
     fun getTracksByName(query: String,playlistId:Int): Flow<List<TrackCache>>

    @Query("SELECT * FROM tracks_table " +
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId "+
            "ORDER BY artistName")
     fun getTracksByArtist(query: String,playlistId:Int):Flow<List<TrackCache>>

    @Query("SELECT * FROM tracks_table WHERE name = :name AND artistName = :artistName")
    suspend fun contains( name: String, artistName: String): TrackCache?

    @Query("SELECT * FROM tracks_table WHERE id = :id")
    suspend fun getById(id: Int): TrackCache?

    @Query("DELETE FROM tracks_table WHERE id =:id")
    suspend fun removeTrack(id: Int)

    @Query("DELETE FROM tracks_table WHERE id NOT IN (:items)")
    suspend fun deleteItemsNotInList(items: List<Int>)

}