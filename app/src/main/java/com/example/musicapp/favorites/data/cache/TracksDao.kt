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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfTracks(track: List<TrackCache>)

//, CASE WHEN cached_tracks.trackId IS NOT NULL THEN 1 ELSE 0 END AS isCached
    @Query("SELECT tracks_table.trackId, url, name, artistName, bigImgUrl, smallImgUrl, albumName, date, durationFormatted, durationInMillis, ownerId " +
            "FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId " +
           // "LEFT JOIN cached_tracks ON cached_tracks.trackId = tracks_table.trackId " +
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId " +
            "ORDER BY date DESC")
    fun getAllTracksByTime(query: String, playlistId: String): Flow<List<TrackCache>>

    @Query("SELECT tracks_table.trackId, url, name, artistName, bigImgUrl, smallImgUrl, albumName, date, durationFormatted, durationInMillis, ownerId " +
            "FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId "+
         //   "LEFT JOIN cached_tracks ON cached_tracks.trackId = tracks_table.trackId " +
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId "+
            "ORDER BY name")
     fun getTracksByName(query: String,playlistId:String): Flow<List<TrackCache>>

    @Query("SELECT tracks_table.trackId, url, name, artistName, bigImgUrl, smallImgUrl, albumName, date, durationFormatted, durationInMillis, ownerId " +
            "FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId "+
           // "LEFT JOIN cached_tracks ON cached_tracks.trackId = tracks_table.trackId " +
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId "+
            "ORDER BY artistName")
     fun getTracksByArtist(query: String,playlistId:String): Flow<List<TrackCache>>

//    @Query("SELECT * FROM tracks_table WHERE name = :name AND artistName = :artistName")
//    suspend fun contains( name: String, artistName: String): TrackCache?

    @Query("SELECT tracks_table.trackId, url, name, artistName, bigImgUrl, smallImgUrl, albumName, date, durationFormatted, durationInMillis, ownerId " +
            "FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON  playlists_and_tracks_table.trackId = tracks_table.trackId " +
            "LEFT JOIN friend_and_tracks_relations ON friend_and_tracks_relations.trackId = tracks_table.trackId " +
            "WHERE name = :name AND artistName = :artistName AND playlistId = :mainPlaylistId AND friend_and_tracks_relations.friendId IS NULL")
    suspend fun contains( name: String, artistName: String, mainPlaylistId: Int): TrackCache?

    @Query("SELECT * FROM tracks_table WHERE trackId = :id")
    suspend fun getById(id: Int): TrackCache?

    @Query("DELETE FROM tracks_table WHERE trackId =:id")
    suspend fun removeTrack(id: Int)




    @Query("SELECT tracks_table.trackId, url, name, artistName, bigImgUrl, smallImgUrl, albumName, date, durationFormatted, durationInMillis, ownerId " +
            "FROM tracks_table " +
            "INNER JOIN friend_and_tracks_relations ON friend_and_tracks_relations.trackId = tracks_table.trackId " +
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND friendId = :friendId " +
            "ORDER BY date DESC "// +
           // "LIMIT :count OFFSET :offset"
    )
     fun getTracksOfFriend(query: String/*,offset: Int, count: Int,*/,friendId: Int): List<TrackCache>



    @Transaction
    suspend fun insertAndDeleteTracksWithSinglePlaylistTransaction(
        tracksToInsert: List<TrackCache>,
        playlistId: String
    ) {
        //tracksToInsert.chunked(500) {
            deleteTracksNotInListWithSinglePlaylist(playlistId)
        //}
        insertListOfTracks(tracksToInsert)
    }


//@Query("DELETE FROM tracks_table " +
//        "WHERE trackId NOT IN (:items) AND NOT EXISTS " +
//        "(SELECT 1 FROM playlists_and_tracks_table " +
//        "WHERE trackId = tracks_table.trackId AND playlistId != :playlistId) " +
//        "AND NOT EXISTS " +
//        "(SELECT 1 FROM friend_and_tracks_relations " +
//        "WHERE trackId = tracks_table.trackId)")
@Query("DELETE FROM tracks_table " +
        "WHERE NOT EXISTS " +
        "(SELECT 1 FROM playlists_and_tracks_table " +
        "WHERE trackId = tracks_table.trackId AND playlistId != :playlistId) " +
        "AND NOT EXISTS " +
        "(SELECT 1 FROM friend_and_tracks_relations " +
        "WHERE trackId = tracks_table.trackId)")
    suspend fun deleteTracksNotInListWithSinglePlaylist( playlistId: String)

//    @Query("DELETE FROM tracks_table " +
//            "WHERE trackId NOT IN (:items) AND trackId IN " +
//            "(SELECT trackId FROM friend_and_tracks_relations WHERE friendId = :friendId LIMIT :listSize)")
//    suspend fun deleteTracksOfFriendNotInList(items: List<String>, friendId: Int,listSize: Int)

    @Query("DELETE FROM tracks_table " +
            "WHERE trackId IN " +
            "(SELECT trackId FROM friend_and_tracks_relations WHERE friendId = :friendId)")
    suspend fun deleteTracksOfFriendNotInList( friendId: Int)
}