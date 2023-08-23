package com.example.musicapp.frienddetails.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by HP on 18.08.2023.
 **/
@Dao
interface FriendsAndTracksRelationsDao {

    companion object{
        const val table_name = "friend_and_tracks_relations"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelationsList(list: List<FriendAndTracksRelation>)

    @Query("SELECT COUNT(*) FROM friend_and_tracks_relations WHERE friendId = :friendId")
    fun friendTrackCount(friendId: Int): Int


    @Query("DELETE FROM friend_and_tracks_relations WHERE  friendId =:friendId")
    fun deleteRelationsNotInList(friendId: Int)

    @Query("SELECT * FROM friend_and_tracks_relations")
    suspend fun selectAll(): List<FriendAndTracksRelation>
}