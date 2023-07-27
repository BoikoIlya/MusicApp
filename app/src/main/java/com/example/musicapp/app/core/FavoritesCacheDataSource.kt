package com.example.musicapp.app.core

/**
 * Created by HP on 09.07.2023.
 **/
interface FavoritesCacheDataSource<T> {

    suspend fun contains(data: Pair<String, String>): Boolean

    suspend fun insertWithNewId(newId: Int, data: T)

    suspend fun insert(data: T)

    suspend fun removeFromDB(id: Int)

    suspend fun update(list: List<T>)

    suspend fun getById(id:Int): T
}