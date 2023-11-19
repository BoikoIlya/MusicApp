package com.kamancho.melisma.trending.domain

import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.trending.data.TrendingRepository
import com.kamancho.melisma.trending.presentation.TrendingResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface MusicInteractor {

    suspend fun fetchData(): Flow<TrendingResult>


}

interface TrendingInteractor : MusicInteractor {


    class Base @Inject constructor(
        private val repository: TrendingRepository,
        private val handleUnauthorizedResponse: HandleResponse,
    ) : TrendingInteractor {

        override suspend fun fetchData(): Flow<TrendingResult> = flow {
            handleUnauthorizedResponse.handle(
                {
                    val topBarItems = repository.fetchTopBarItems().sortedBy { it.sortPriority() }
                    val embeddedPlaylists = repository.fetchEmbeddedVkPlaylists()
                    emit(TrendingResult.Success(Triple(topBarItems, emptyList(),embeddedPlaylists)))
                    val tracks = repository.fetchTracks()
                    emit(TrendingResult.Success(Triple(topBarItems, tracks,embeddedPlaylists)))
                },
                { errorMessage, _ ->
                    emit(
                        TrendingResult.Error(
                            errorMessage,
                            repository.fetchTopBarItems().sortedBy { it.sortPriority() },repository.fetchEmbeddedVkPlaylists())
                    )
                }
            )
        }


    }

}
