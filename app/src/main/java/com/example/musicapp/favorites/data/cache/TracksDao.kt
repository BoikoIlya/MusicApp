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
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackCache)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListOfTracks(track: List<TrackCache>)

    @Query("SELECT * FROM tracks_table WHERE name LIKE '%' || :query || '%' ORDER BY date DESC")
     fun getTracksByTime(query: String): Flow<List<TrackCache>>

    @Query("SELECT * FROM tracks_table WHERE name LIKE '%' || :query || '%' ORDER BY name")
     fun getTracksByName(query: String): Flow<List<TrackCache>>

    @Query("SELECT * FROM tracks_table WHERE name LIKE '%' || :query || '%' ORDER BY artistName")
     fun getTracksByArtist(query: String):Flow<List<TrackCache>>

    @Query("SELECT * FROM tracks_table WHERE url =:itemUrl")
    suspend fun contains(itemUrl: String): TrackCache?

    @Query("DELETE FROM tracks_table WHERE id = :itemId")
    suspend fun removeTrack(itemId: String)

    @Query("DELETE FROM tracks_table WHERE id NOT IN (:items)")
    fun deleteItemsNotInList(items: List<Int>)

}