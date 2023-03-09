package com.example.musicapp.app.core


/**
 * Created by HP on 27.01.2023.
 **/
interface Repository<T> {

    suspend fun fetchData():T
}