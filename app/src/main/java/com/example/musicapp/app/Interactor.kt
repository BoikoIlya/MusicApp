package com.example.musicapp.app

/**
 * Created by HP on 26.01.2023.
 **/
interface Interactor<T> {

    suspend fun fetchData(): T


}