package com.example.musicapp.trending.domain

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.playlist.data.cache.PlaylistIdTransfer
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.presentation.TrendingResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface MusicInteractor{

    suspend fun fetchData():TrendingResult

   suspend fun checkForNewQueue(): List<MediaItem>

   abstract class Abstract(
       protected val tempCache: TemporaryTracksCache
   ): MusicInteractor{
       override suspend fun checkForNewQueue(): List<MediaItem> = tempCache.map()

   }

}
interface TrendingInteractor: MusicInteractor {

    fun savePlaylistId(id: String)

    class Base @Inject constructor(
        private val repository: TrendingRepository,
        private val mapper: TrackDomain.Mapper<MediaItem>,
        private val handleUnauthorizedResponse: HandleResponse,
        private val transfer: PlaylistIdTransfer,
        tempCache: TemporaryTracksCache
    ): TrendingInteractor, MusicInteractor.Abstract(tempCache){

        override suspend fun fetchData(): TrendingResult =
          handleUnauthorizedResponse.handle(
              {
                  coroutineScope {
                      val playlists = async { repository.fetchPlaylists() }
                      val tracks = async { repository.fetchTracks() }
                      tempCache.saveCurrentPageTracks(tracks.await().map { it.map(mapper) })
                      return@coroutineScope TrendingResult.Success(Pair(playlists.await(),tracks.await()))
                  }
              },
              { errorMessage, _ ->
                  return@handle TrendingResult.Error(errorMessage)
              }
          )

        override fun savePlaylistId(id: String) = transfer.save(id)

    }

}
