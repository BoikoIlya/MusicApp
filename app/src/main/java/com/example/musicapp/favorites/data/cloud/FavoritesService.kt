package com.example.musicapp.favorites.data.cloud

import com.example.musicapp.app.vkdto.TracksCloud
import com.example.musicapp.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 17.06.2023.
 **/
interface FavoritesService {

    @GET("/method/audio.get")
    suspend fun getTracks(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("count") count: Int,
        @Query("offset") offset: Int =0,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v")  apiVersion: String = AppModule.api_version,
    ): TracksCloud

    @GET("/method/audio.getCount")
    suspend fun getTracksCount(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): CountResponse

    @GET("/method/audio.add")
    suspend fun addTrackToFavorites(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") track_owner_id: Int,
        @Query("audio_id") audio_id: Int,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): TrackIdResponse

    @GET("/method/audio.delete")
    suspend fun deleteTrackFromFavorites(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") account_owner_id: String,
        @Query("audio_id") audio_id: Int,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): TrackIdResponse

}