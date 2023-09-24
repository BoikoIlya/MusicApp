package com.kamancho.melisma.favorites.data.cache

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

    @Query("SELECT tracks_table.trackId, url, name, artistName, bigImgUrl, smallImgUrl, albumName, date, durationFormatted, durationInMillis, ownerId " +
            "FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId " +
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId " +
            "ORDER BY date DESC "+
            "LIMIT :limit OFFSET :offset "
    )
    fun getAllTracksByTime(query: String, playlistId: String, limit: Int, offset: Int): Flow<List<TrackCache>>

    @Query("SELECT tracks_table.trackId, url, name, artistName, bigImgUrl, smallImgUrl, albumName, date, durationFormatted, durationInMillis, ownerId " +
            "FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId "+
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId "+
            "ORDER BY name "+
            "LIMIT :limit OFFSET :offset "
    )
     fun getTracksByName(query: String,playlistId:String, limit: Int, offset: Int): Flow<List<TrackCache>>

    @Query("SELECT tracks_table.trackId, url, name, artistName, bigImgUrl, smallImgUrl, albumName, date, durationFormatted, durationInMillis, ownerId " +
            "FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId "+
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND playlistId = :playlistId "+
            "ORDER BY artistName "+
            "LIMIT :limit OFFSET :offset "
    )
     fun getTracksByArtist(query: String,playlistId:String, limit: Int, offset: Int): Flow<List<TrackCache>>


    @Query("SELECT tracks_table.trackId, url, name, artistName, bigImgUrl, smallImgUrl, albumName, date, durationFormatted, durationInMillis, ownerId " +
            "FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON  playlists_and_tracks_table.trackId = tracks_table.trackId " +
            "WHERE name = :name AND artistName = :artistName AND playlistId = :mainPlaylistId ")
    suspend fun contains( name: String, artistName: String, mainPlaylistId: Int): TrackCache?

    @Query("SELECT * FROM tracks_table WHERE trackId = :id")
    suspend fun getById(id: Int): TrackCache?

    @Query("DELETE FROM tracks_table WHERE trackId =:id")
    suspend fun removeTrack(id: Int)

    @Query("SELECT COUNT(*) " +
            "FROM tracks_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.trackId = tracks_table.trackId "+
            "WHERE  playlistId = :playlistId ")
    suspend fun count(playlistId:String): Int


    @Query("SELECT tracks_table.trackId, url, name, artistName, bigImgUrl, smallImgUrl, albumName, date, durationFormatted, durationInMillis, ownerId " +
            "FROM tracks_table " +
            "INNER JOIN friend_and_tracks_relations ON friend_and_tracks_relations.trackId = tracks_table.trackId " +
            "WHERE (name LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%') AND friendId = :friendId " +
            "ORDER BY date DESC "
    )
     fun getTracksOfFriend(query: String,friendId: Int): List<TrackCache>



    @Transaction
    suspend fun insertAndDeleteTracksWithSinglePlaylistTransaction(
        tracksToInsert: List<TrackCache>,
        playlistId: String
    ) {

            deleteTracksNotInListWithSinglePlaylist(playlistId)
        insertListOfTracks(tracksToInsert)
    }


@Query("DELETE FROM tracks_table " +
        "WHERE NOT EXISTS " +
        "(SELECT 1 FROM playlists_and_tracks_table " +
        "WHERE trackId = tracks_table.trackId AND playlistId != :playlistId) " +
        "AND NOT EXISTS " +
        "(SELECT 1 FROM friend_and_tracks_relations " +
        "WHERE trackId = tracks_table.trackId)")
    suspend fun deleteTracksNotInListWithSinglePlaylist( playlistId: String)


    @Query("DELETE FROM tracks_table " +
            "WHERE trackId IN " +
            "(SELECT trackId FROM friend_and_tracks_relations WHERE friendId = :friendId)")
    suspend fun deleteTracksOfFriendNotInList( friendId: Int)
}