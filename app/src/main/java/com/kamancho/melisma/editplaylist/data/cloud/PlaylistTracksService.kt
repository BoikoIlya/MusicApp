package com.kamancho.melisma.editplaylist.data.cloud

import com.kamancho.melisma.app.vkdto.FavoritePlaylistByIdResponse
import com.kamancho.melisma.app.vkdto.SearchPlaylistByIdResponse
import com.kamancho.melisma.app.vkdto.TracksCloud
import com.kamancho.melisma.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistTracksService {

    @GET("/method/audio.get")
    suspend fun getPlaylistTracks(
        @Query("access_token")  accessToken: String,
        @Query("album_id")  album_id: String,
        @Query("owner_id") owner_id: String,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v")  apiVersion: String = AppModule.api_version,
    ): TracksCloud



    @GET("/method/audio.getPlaylistById")
    suspend fun getFavoritePlaylistById(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("playlist_id")  playlist_id: String,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v") apiVersion: String = AppModule.api_version,
    ): FavoritePlaylistByIdResponse

    @GET("/method/audio.getPlaylistById")
    suspend fun getSearchPlaylistById(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("playlist_id")  playlist_id: String,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v") apiVersion: String = AppModule.api_version,
    ): SearchPlaylistByIdResponse
}