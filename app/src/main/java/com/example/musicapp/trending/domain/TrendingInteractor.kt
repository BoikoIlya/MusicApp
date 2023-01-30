package com.example.musicapp.trending.domain

import com.example.musicapp.app.HandleError
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.presentation.TrendingResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface TrendingInteractor {

    suspend fun fetchData(): TrendingResult

    class Base @Inject constructor(
        private val repository: TrendingRepository,
        private val handleError: HandleError
    ): TrendingInteractor{

        override suspend fun fetchData(): TrendingResult =
            try {
            coroutineScope {
                    val playlists = async { repository.fetchPlaylists() }
                    val tracks = async { repository.fetchTracks() }
                    return@coroutineScope TrendingResult.Success(Pair(playlists.await(),tracks.await()))
                }
            } catch (e: Exception) {
                 TrendingResult.Error(handleError.handle(e))
            }
    }

}
