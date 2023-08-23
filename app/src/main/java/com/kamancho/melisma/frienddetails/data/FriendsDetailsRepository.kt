package com.kamancho.melisma.frienddetails.data

import com.kamancho.melisma.app.core.Mapper

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendsDetailsRepository {

    suspend fun update(friendId: String)

    abstract class Abstract<T,C>(
        private val mapper: Mapper<Pair<String,List<T>>, List<C>>
    ): FriendsDetailsRepository {

        override suspend fun update(friendId: String)
        = saveToCache(mapper.map(Pair(friendId,cloudData(friendId))),friendId)


        protected abstract suspend fun cloudData(friendId: String): List<T>

        protected abstract suspend fun saveToCache(list: List<C>,friendId: String)
    }


}