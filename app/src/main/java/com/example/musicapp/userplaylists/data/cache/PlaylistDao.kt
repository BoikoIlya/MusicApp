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

    @Query("SELECT * FROM playlists_table" +
            " WHERE title LIKE '%' || :query || '%' AND playlistId != :mainPlaylistId" +
            " ORDER BY create_time DESC"
    )
    fun getPlaylistsOrderByCreateTime(query: String, mainPlaylistId: Int): Flow<List<PlaylistCache>>

    @Query("SELECT * FROM playlists_table" +
            " WHERE title LIKE '%' || :query || '%' AND playlistId != :mainPlaylistId AND is_following = :isFollowing" +
            " ORDER BY create_time DESC"
    )
    fun getPlaylistsFollowedOrNotOrderByCreateTime(query: String, mainPlaylistId: Int, isFollowing:Boolean): Flow<List<PlaylistCache>>

    @Query("SELECT * FROM playlists_table WHERE title = :name")
    suspend fun contains(name: String):PlaylistCache?

    @Query("DELETE FROM playlists_table WHERE playlistId = :id")
    suspend fun deletePlaylist(id: Int)

    @Query("SELECT * FROM playlists_table WHERE  playlistId = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlaylistCache?

    @Query("DELETE FROM playlists_table WHERE playlistId NOT IN (:list)")
    suspend fun deletePlaylistsNotInList(list: List<Int>)
}