package com.example.musicapp.search.data.cloud

import com.example.musicapp.app.vkdto.TracksCloud
import com.example.musicapp.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 29.04.2023.
 **/
interface SearchTrackService {



    @GET("/method/audio.search")
    suspend fun searchTrack(
        @Query("access_token")  accessToken: String,
        @Query("q") query: String,
        @Query("count") count: Int,
        @Query("offset") offset: Int,
        @Query("v")  apiVersion: String = AppModule.api_version,
    ): TracksCloud
}