package com.example.musicapp.trending.presentation

import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 28.01.2023.
 **/
class TestTrendingViewModel {

    lateinit var viewModel: TrendingViewModel

    @Before
    fun setup(){

    }

    @Test
    fun `test init`() {

    }


    class TestDispatcherList(
        private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    )
}