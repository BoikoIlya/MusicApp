package com.kamancho.melisma.artisttracks.data

import android.util.Log
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.artisttracks.data.cache.ArtistsTracksCacheDataSource
import com.kamancho.melisma.artisttracks.data.cloud.ArtistTracksService
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.main.data.cache.AccountDataStore
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
interface ArtistTracksRepository {


    suspend fun fetchTracks(artistId: String): List<TrackItem>

    suspend fun readTrackId(): String

    suspend fun saveTrackId(trackId: String)

    class Base @Inject constructor(
        private val service: ArtistTracksService,
        private val cache: ArtistsTracksCacheDataSource,
     private val accountDataStore: AccountDataStore,
        private val captchaDataStore: CaptchaDataStore,
    ) : ArtistTracksRepository {

        override suspend fun fetchTracks(artistId: String): List<TrackItem> {
            val cached = cache.readTracks(artistId)
            Log.d("fetchTracks", "${cached.size} ")
            return cached.ifEmpty {
                Log.d("fetchTracks", " cloud ${cached.size} ")
               val result = service.getAudiosByArtist(
                    accountDataStore.token(),
                    artistId,
                    captchaDataStore.captchaId(),
                    captchaDataStore.captchaEnteredData()
                ).response.items
                cache.saveTracks(artistId,result)
                result
            }
        }

        override suspend fun readTrackId(): String = cache.readTrackId()

        override suspend fun saveTrackId(trackId: String) {
            cache.clearCache()
            cache.saveTrackId(trackId)
        }
    }
}