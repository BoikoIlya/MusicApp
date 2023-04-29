package com.example.musicapp.trending.domain

import androidx.media3.common.MediaItem
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.app.core.HandleError
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.main.data.AuthorizationRepository
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.presentation.TrendingResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import javax.inject.Inject

interface MusicInteractor<T>: Interactor<T>{

   suspend fun checkForNewQueue(): List<MediaItem>

   abstract class Abstract<T>(
       protected val tempCache: TemporaryTracksCache
   ): MusicInteractor<T>{
       override suspend fun checkForNewQueue(): List<MediaItem> = tempCache.map()

   }

}
interface TrendingInteractor: MusicInteractor<TrendingResult> {

    class Base @Inject constructor(
        private val repository: TrendingRepository,
        private val handleError: HandleError,
        private val mapper: TrackDomain.Mapper<MediaItem>,
        private val auth: AuthorizationRepository,
        tempCache: TemporaryTracksCache
    ): TrendingInteractor, MusicInteractor.Abstract<TrendingResult>(tempCache){

        companion object{
            private const val unauthorized_response = 401
        }
        override suspend fun fetchData(): TrendingResult =
            try {
            coroutineScope {
                    val playlists = async { repository.fetchPlaylists() }
                    val tracks = async { repository.fetchTracks() }
                    tempCache.saveCurrentPageTracks(tracks.await().map { it.map(mapper) })
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
