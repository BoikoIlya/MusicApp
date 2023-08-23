package com.kamancho.melisma.frienddetails.data.cache

import androidx.room.Entity

/**
 * Created by HP on 19.08.2023.
 **/
@Entity(
    primaryKeys = ["playlistId","friendId"],
    tableName = FriendsAndPlaylistsRelationDao.table_name,
)
data class FriendAndPlaylistRelation(
    val playlistId: String,
    val friendId: Int
)
