package com.kamancho.melisma.friends.data

import com.kamancho.melisma.friends.data.cache.FriendCache
import com.kamancho.melisma.friends.data.cache.FriendsDao
import com.kamancho.melisma.friends.data.cloud.FriendsCloudDataSource
import kotlinx.coroutines.flow.Flow
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