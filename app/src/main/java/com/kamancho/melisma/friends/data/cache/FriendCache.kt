package com.kamancho.melisma.friends.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by HP on 17.08.2023.
 **/
@Entity(tableName = FriendsDao.table_name)
data class FriendCache(
    @PrimaryKey(autoGenerate = false)
    val friendId: Int,
    val firstName: String,
    val secondName: String,
    val photoUrl: String
)
