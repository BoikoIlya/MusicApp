package com.example.musicapp.app.core

import com.example.musicapp.favorites.data.cache.TrackCache

/**
 * Created by HP on 09.07.2023.
 **/
interface FavoritesCacheDataSource<T> {

    suspend fun contains(nameAndAuthor: Pair<String, String>): Boolean

    suspend fun insertWithNewId(id: Int, data: T)

    suspend fun insert(trackCache: T)

    suspend fun removeFromDB(id: Int)

    suspend fun update(list: List<T>)

    suspend fun getById(id:Int): T
}