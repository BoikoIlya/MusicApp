package com.example.musicapp.frienddetails.data

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.app.vkdto.TracksCloud
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cloud.FavoritesService
import com.example.musicapp.frienddetails.data.cloud.FriendsDetailsCloudDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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