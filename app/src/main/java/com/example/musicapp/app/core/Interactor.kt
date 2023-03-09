package com.example.musicapp.app.core

/**
 * Created by HP on 26.01.2023.
 **/
interface Interactor<T> {

    suspend fun fetchData(): T


}