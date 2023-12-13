package com.kamancho.melisma.artisttracks.data.cloud

import com.kamancho.melisma.app.vkdto.TracksCloud
import com.kamancho.melisma.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
interface ArtistTracksService {

    @GET("/method/audio.getAudiosByArtist")
    suspend fun getAudiosByArtist(
        @Query("access_token") accessToken: String,
        @Query("artist_id") artist_id: String,
        @Query("captcha_sid") captcha_sid: String,
        @Query("captcha_key") captcha_key: String,
        @Query("v") apiVersion: String = AppModule.api_version,
    ): TracksCloud
}