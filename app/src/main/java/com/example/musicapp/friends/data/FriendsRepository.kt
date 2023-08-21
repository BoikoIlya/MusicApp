package com.example.musicapp.friends.data

import com.example.musicapp.friends.data.cache.FriendCache
import com.example.musicapp.friends.data.cache.FriendsDao
import com.example.musicapp.friends.data.cloud.FriendsCloudDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 17.08.2023.
 **/
interface FriendsRepository {

    suspend fun updateFriends()

    suspend fun search(query: String): Flow<List<FriendCache>>

    class Base @Inject constructor(
        private val cloudDataSource: FriendsCloudDataSource,
        private val cache: FriendsDao,
    ): FriendsRepository {

        override suspend fun updateFriends() {
            val cloudResult = cloudDataSource.fetchFriends()
            val itemsToSave = cloudResult.filter { !it.isDeactivated() }.map { it.map() }
            cache.clearAndInsertFriendsTransaction(itemsToSave)
        }

        override suspend fun search(query: String): Flow<List<FriendCache>> = cache.search(query)
    }
}