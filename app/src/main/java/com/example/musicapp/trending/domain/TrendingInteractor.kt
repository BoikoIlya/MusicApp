package com.example.musicapp.trending.domain

import androidx.media3.common.MediaItem
import com.example.musicapp.app.main.data.TemporaryTracksCache
import com.example.musicapp.app.core.HandleError
import com.example.musicapp.app.main.data.AuthorizationRepository
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.presentation.TrendingResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import javax.inject.Inject

interface TrendingInteractor {

    suspend fun fetchData(): TrendingResult


    class Base @Inject constructor(
        private val repository: TrendingRepository,
        private val handleError: HandleError,
        private val mapper: TrackDomain.Mapper<MediaItem>,
        private val tempCache: TemporaryTracksCache,
        private val auth: AuthorizationRepository
    ): TrendingInteractor{

        companion object{
            private const val unauthorized_response = 401
        }

        override suspend fun fetchData(): TrendingResult =
            try {
            coroutineScope {
                    val playlists = async { repository.fetchPlaylists() }
                    val tracks = async { repository.fetchTracks() }
                    tempCache.saveTracks(tracks.await().map { it.map(mapper) })
                    return@coroutineScope TrendingResult.Success(Pair(playlists.await(),tracks.await()))
                }
            } catch (e: Exception) {
                if(e is HttpException && e.code() == unauthorized_response){
                    try {
                        auth.updateToken()
                    }catch (e: Exception) {
                        TrendingResult.Error(handleError.handle(e))
                    }
                    fetchData()
                }else TrendingResult.Error(handleError.handle(e))
            }

    }

}
