package com.example.musicapp.trending.domain

import com.example.musicapp.core.HandleError
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.presentation.TrendingResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

interface TrendingInteractor {

    suspend fun fetchData(): TrendingResult

    class Base(
        private val repository: TrendingRepository,
        private val handleError: HandleError
    ): TrendingInteractor{

        override suspend fun fetchData(): TrendingResult =
            coroutineScope {
                try {
                    val playlists = async { repository.fetchPlaylists() }
                    val tracks = async { repository.fetchTracks() }
                    return@coroutineScope TrendingResult.Success(Pair(playlists.await(),tracks.await()))
                } catch (e: Exception) {
                    return@coroutineScope TrendingResult.Error(handleError.handle(e))
                }
            }
    }

}
