package com.example.musicapp.favorites.testcore

import com.example.musicapp.app.core.DispatchersList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * Created by HP on 28.04.2023.
 **/

@ExperimentalCoroutinesApi
class TestDispatcherList(
    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
): DispatchersList {
    override fun io(): CoroutineDispatcher = dispatcher

    override fun ui(): CoroutineDispatcher = dispatcher

}