package com.kamancho.melisma.trending.domain

import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.favoritesplaylistdetails.data.cache.PlaylistIdTransfer
import com.kamancho.melisma.trending.data.TrendingRepository
import com.kamancho.melisma.trending.presentation.TrendingResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface MusicInteractor{

    suspend fun fetchData(): Flow<TrendingResult>




}
interface TrendingInteractor: MusicInteractor {

    fun savePlaylistId(id: String)

    fun resetOffset()

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
        override fun resetOffset() = repository.resetOffset()

    }

}
