package com.example.musicapp.app.core

/**
 * Created by HP on 09.07.2023.
 **/
interface FavoritesCloudDataSource<T> {

    suspend fun addToFavorites(data: Pair<Int, Int>): Int

    suspend fun removeFromFavorites(id: Int)

    suspend fun favorites(): List<T>
}