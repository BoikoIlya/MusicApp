package com.example.musicapp.trending.domain

import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.favoritesplaylistdetails.data.cache.PlaylistIdTransfer
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.presentation.TrendingResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface MusicInteractor{

    suspend fun fetchData():TrendingResult




}
interface TrendingInteractor: MusicInteractor {

    fun savePlaylistId(id: String)

    class Base @Inject constructor(
        private val repository: TrendingRepository,
        private val handleUnauthorizedResponse: HandleResponse,
        private val transfer: PlaylistIdTransfer,
    ): TrendingInteractor{

        override suspend fun fetchData(): TrendingResult =
          handleUnauthorizedResponse.handle(
              {
                  coroutineScope {
                      val playlists = async { repository.fetchTopBarItems().sortedBy { it.sortPriority() } }
                      val tracks = async { repository.fetchTracks() }
                      return@coroutineScope TrendingResult.Success(Pair(playlists.await(),tracks.await()))
                  }
              },
              { errorMessage, _ ->
                  return@handle TrendingResult.Error(errorMessage,repository.fetchTopBarItems().sortedBy { it.sortPriority() })
              }
          )

        override fun savePlaylistId(id: String) = transfer.save(id)

    }

}
