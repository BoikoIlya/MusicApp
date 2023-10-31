package com.kamancho.melisma.friends.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 17.08.2023.
 **/
@Dao
interface FriendsDao {

    companion object{
        const val table_name = "friends_table"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriendsList(list: List<FriendCache>)

    @Query("SELECT * FROM friends_table " +
            "WHERE (firstName LIKE '%' || :query || '%' OR secondName LIKE '%' || :query || '%') " +
            "ORDER BY secondName")
     fun search(query: String): Flow<List<FriendCache>>

     @Query("DELETE FROM friends_table")
     suspend fun deleteFriends()

     @Transaction
     suspend fun clearAndInsertFriendsTransaction(list: List<FriendCache>){
         deleteFriends()
         insertFriendsList(list)
     }
}