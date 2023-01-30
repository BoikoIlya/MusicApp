package com.example.musicapp.app


/**
 * Created by HP on 27.01.2023.
 **/
interface Repository<T> {

    suspend fun fetchData():T
}