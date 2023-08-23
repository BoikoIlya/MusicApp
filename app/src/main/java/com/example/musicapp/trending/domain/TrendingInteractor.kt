package com.example.musicapp.trending.domain

import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.favoritesplaylistdetails.data.cache.PlaylistIdTransfer
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.presentation.TrendingResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface MusicInteractor{

    suspend fun fetchData(): Flow<TrendingResult>




}
interface TrendingInteractor: MusicInteractor {

    fun savePlaylistId(id: String)

    class Base @Inject constructor(
        private val repository: TrendingRepository,
        private val handleUnauthorizedResponse: HandleResponse,
        private val transfer: PlaylistIdTransfer,
    ): TrendingInteractor{

        override suspend fun fetchData(): Flow<TrendingResult> = flow {
          handleUnauthorizedResponse.handle(
              {
                      val topBarItems = repository.fetchTopBarItems().sortedBy { it.sortPriority() }
                      emit(TrendingResult.Success(Pair(topBarItems, emptyList())))
                      val tracks =  repository.fetchTracks()
                      emit(TrendingResult.Success(Pair(topBarItems, tracks)))
              },
              { errorMessage, _ ->
                  emit(TrendingResult.Error(errorMessage,repository.fetchTopBarItems().sortedBy { it.sortPriority() }))
              }
          ) }

        override fun savePlaylistId(id: String) = transfer.save(id)

    }

}
