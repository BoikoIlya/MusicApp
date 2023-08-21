package com.example.musicapp.userplaylists.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 12.07.2023.
 **/
@Dao
interface PlaylistDao {

    companion object{
        const val playlists_table = "playlists_table"

    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistCache: PlaylistCache)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfPlaylists(list: List<PlaylistCache>)

    @Query("SELECT playlists_table.playlistId, title, is_following, count, update_time, description, owner_id, thumbs " +
            "FROM playlists_table " +
            "LEFT JOIN friends_and_playlists_table ON friends_and_playlists_table.playlistId = playlists_table.playlistId " +
            "WHERE title LIKE '%' || :query || '%' AND playlists_table.playlistId != :mainPlaylistId AND friends_and_playlists_table.friendId IS NULL" +
            " ORDER BY update_time DESC"
    )
    fun getPlaylistsOrderByUpdateTime(query: String, mainPlaylistId: String): Flow<List<PlaylistCache>>

    @Query("SELECT playlists_table.playlistId, title, is_following, count, update_time, description, owner_id, thumbs " +
            "FROM playlists_table " +
            "LEFT JOIN friends_and_playlists_table ON friends_and_playlists_table.playlistId = playlists_table.playlistId " +
            " WHERE title LIKE '%' || :query || '%' AND playlists_table.playlistId != :mainPlaylistId AND is_following = :isFollowing AND friends_and_playlists_table.friendId IS NULL" +
            " ORDER BY update_time DESC"
    )
    fun getPlaylistsFollowedOrNotOrderByUpdateTime(query: String, mainPlaylistId: String, isFollowing:Boolean): Flow<List<PlaylistCache>>

    @Query("SELECT playlists_table.playlistId, title, is_following, count, update_time, description, owner_id, thumbs " +
            "FROM playlists_table " +
            "LEFT JOIN friends_and_playlists_table ON friends_and_playlists_table.playlistId = playlists_table.playlistId " +
            "WHERE title = :title AND friends_and_playlists_table.friendId IS NULL")
    suspend fun contains(title: String):PlaylistCache?

    @Query("DELETE FROM playlists_table WHERE playlistId = :id")
    suspend fun deletePlaylist(id: String)

    @Query("SELECT * FROM playlists_table WHERE  playlistId = :playlistId")
    fun getPlaylistById(playlistId: String): Flow<PlaylistCache?>

    //@Query("DELETE FROM playlists_table WHERE playlistId NOT IN (:list) AND playlistId!=:mainPlaylistId")
//    @Query("DELETE FROM playlists_table " +
//            "WHERE playlistId NOT IN (:list) " +
//            "AND playlistId != :mainPlaylistId " +
//            "AND (SELECT COUNT(*) FROM friends_and_playlists_table " +
//            "      WHERE friends_and_playlists_table.playlistId = playlists_table.playlistId " +
//            "      AND friends_and_playlists_table.friendId IS NULL) = 0")
    @Query("DELETE FROM playlists_table WHERE playlistId NOT IN (:list) AND playlistId != :mainPlaylistId " +
            "AND (SELECT COUNT(*) FROM friends_and_playlists_table " +
            "      WHERE friends_and_playlists_table.playlistId = playlists_table.playlistId " +
            "      AND friends_and_playlists_table.friendId IS NULL) = " +
            "(SELECT COUNT(*) FROM friends_and_playlists_table " +
            "WHERE friends_and_playlists_table.playlistId = playlists_table.playlistId)")
    suspend fun deletePlaylistsNotInList(list: List<String>,mainPlaylistId: String)

    @Query("SELECT tracks_table.smallImgUrl FROM playlists_table " +
            "INNER JOIN playlists_and_tracks_table ON playlists_and_tracks_table.playlistId = playlists_table.playlistId " +
            "INNER JOIN tracks_table ON tracks_table.trackId = playlists_and_tracks_table.trackId " +
            "WHERE playlists_and_tracks_table.playlistId = :playlistId AND tracks_table.smallImgUrl IS NOT NULL " +
            "LIMIT 4")
    suspend fun selectFourFirstImagesFromPlaylistTracks(playlistId: String):List<String>

    @Query("DELETE FROM tracks_table " +
            "WHERE trackId IN (" +
            "    SELECT t.trackId " +
            "    FROM tracks_table t " +
            "    INNER JOIN playlists_and_tracks_table pt ON t.trackId = pt.trackId " +
            "    WHERE pt.playlistId = :playlistId " +
            "    AND t.trackId NOT IN (" +
            "        SELECT t2.trackId " +
            "        FROM tracks_table t2 " +
            "        INNER JOIN playlists_and_tracks_table pt2 ON t2.trackId = pt2.trackId " +
            "        WHERE pt2.playlistId != :playlistId" +
            "    )" +
            ")"
    )
    suspend fun deleteTracksWhichAssociatedOnlyWithCurrentPlaylist(playlistId: String)


    @Query("DELETE FROM playlists_table " +
            "WHERE playlistId NOT IN (:items) AND playlistId IN " +
            "(SELECT playlistId FROM friends_and_playlists_table WHERE friendId = :friendId)")
    suspend fun deletePlaylistsOfFriendNotInList(items: List<String>, friendId: Int)


    @Query("SELECT playlists_table.playlistId, title, is_following, count, update_time, description, owner_id, thumbs " +
            "FROM playlists_table " +
            "INNER JOIN friends_and_playlists_table ON friends_and_playlists_table.playlistId = playlists_table.playlistId " +
            " WHERE title LIKE '%' || :query || '%' AND friendId = :friendId " +
            "ORDER BY update_time DESC")
    fun getPlaylistsOfFriend(query: String,friendId: Int): List<PlaylistCache>
}