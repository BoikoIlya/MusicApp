package com.example.musicapp.search.data.cloud

import com.example.musicapp.app.vkdto.SearchPlaylistsCloud
import com.example.musicapp.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 29.04.2023.
 **/

interface SearchPlaylistsService {

    @GET("/method/audio.searchPlaylists")
    suspend fun search(
        @Query("access_token")  accessToken: String,
        @Query("q") query: String,
        @Query("count") count: Int,
        @Query("offset") offset: Int,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v") apiVersion: String = AppModule.api_version,
    ): SearchPlaylistsCloud
}