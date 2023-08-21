package com.example.musicapp.frienddetails.data.cache

import androidx.room.Entity
import com.example.musicapp.favorites.data.cache.PlaylistsAndTracksDao

/**
 * Created by HP on 18.08.2023.
 **/
@Entity(
    primaryKeys = ["trackId","friendId"],
    tableName = FriendsAndTracksRelationsDao.table_name,
)
data class FriendAndTracksRelation(
    val trackId: String,
    val friendId: Int
)
