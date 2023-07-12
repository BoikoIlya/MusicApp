package com.example.musicapp.app.core


import android.util.Log
import com.example.musicapp.main.data.cache.AccountDataStore


/**
 * Created by HP on 27.01.2023.
 **/

interface Repository<T,R> {

    suspend fun containsInDb(data: Pair<String, String>): Boolean

    suspend fun addToFavoritesIfNotDuplicated(data: T): Int

    suspend fun addToFavorites(data: T): Int

    suspend fun userId(): Int

    suspend fun deleteData(id: Int)
}

interface FavoritesRepository<T,R,S>: Repository<T,R> {

    suspend fun updateData()

    abstract class Abstract<T,R,S>(
        private val domainToCacheMapper: Mapper<T,R>,
        private val domainToDataIdsMapper: Mapper<T,Pair<Int,Int>>,
        private val domainToContainsMapper: Mapper<T,Pair<String,String>>,
        private val cloudToCacheMapper: Mapper<List<S>, List<R>>,
        private val transfer: DataTransfer<T>,
        private val accountDataStore: AccountDataStore,
        private val handleResponseData: HandleDeleteRequestData<R>,
        private val cloud: FavoritesCloudDataSource<S>,
        private val cache: FavoritesCacheDataSource<R>
    ): FavoritesRepository<T,R,S>{

        override suspend fun containsInDb(data: Pair<String,String>): Boolean = cache.contains(data)

        override suspend fun addToFavorites(data: T): Int {
            val newId = cloud.addToFavorites(domainToDataIdsMapper.map(data))
            cache.insertWithNewId(newId,domainToCacheMapper.map(data))
            return newId
        }

        override suspend fun deleteData(id: Int) =
            handleResponseData.handle(id,{
                cache.removeFromDB(id)
                cloud.removeFromFavorites(id)
            },{item->
               cache.insert(item)
            })

        override suspend fun addToFavoritesIfNotDuplicated(data: T):Int =
            if(!cache.contains(domainToContainsMapper.map(data))){
               addToFavorites(data)
            }else {
                transfer.save(data)
                throw DuplicateException()
            }

        override suspend fun updateData() {
            val cloudData = cloud.favorites()
            cache.update(cloudToCacheMapper.map(cloudData))
        }


        override suspend fun userId(): Int = accountDataStore.ownerId().toInt()

    }
}

