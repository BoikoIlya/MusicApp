package com.example.musicapp.app

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface DispatchersList {

    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher

    class Base @Inject constructor() : DispatchersList {
        override fun io(): CoroutineDispatcher = Dispatchers.IO
        override fun ui(): CoroutineDispatcher = Dispatchers.Main
    }
}
