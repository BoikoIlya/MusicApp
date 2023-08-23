package com.example.musicapp.frienddetails.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

/**
 * Created by HP on 19.08.2023.
 **/

@Dao
interface FriendsAndPlaylistsRelationDao {

    companion object{
        const val table_name = "friends_and_playlists_table"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<FriendAndPlaylistRelation>)

    @Query("SELECT COUNT(*) FROM friends_and_playlists_table WHERE friendId = :friendId")
    fun friendPlaylistCount(friendId: Int): Int


    @Query("DELETE FROM friends_and_playlists_table WHERE friendId = :friendId ")
    fun deleteRelations(friendId: Int)

    @Transaction
    suspend fun clearAndInsertRelations(items: List<FriendAndPlaylistRelation>,friendId: Int){
        deleteRelations(friendId)
        insertList(items)
    }
}