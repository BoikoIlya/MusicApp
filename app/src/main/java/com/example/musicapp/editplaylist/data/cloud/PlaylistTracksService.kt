package com.example.musicapp.editplaylist.data.cloud

import com.example.musicapp.app.vkdto.TracksCloud
import com.example.musicapp.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistTracksService {

    @GET("/method/audio.get")
    suspend fun getPlaylistTracks(
        @Query("access_token")  accessToken: String,
        @Query("album_id")  album_id: Int,
        @Query("v")  apiVersion: String = AppModule.api_version,
    ): TracksCloud
}