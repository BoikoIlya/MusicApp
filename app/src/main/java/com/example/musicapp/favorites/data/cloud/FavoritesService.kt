package com.example.musicapp.favorites.data.cloud

import com.example.musicapp.app.vkdto.TracksCloud
import com.example.musicapp.main.di.AppModule
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 17.06.2023.
 **/
interface FavoritesService {

    @GET("/method/audio.get")
    suspend fun getFavoritesTracks(
        @Query("access_token")  accessToken: String,
        @Query("count") count: Int,
        @Query("v")  apiVersion: String = AppModule.api_version,
//        @Query("captcha_sid") captcha_sid:String="1",
//        @Query("captcha_key") captcha_key:String="1"
    ): TracksCloud

    @GET("/method/audio.getCount")
    suspend fun getTracksCount(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): CountResponse

    @GET("/method/audio.add")
    suspend fun addTrackToFavorites(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") track_owner_id: Int,
        @Query("audio_id") audio_id: Int,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): TrackIdResponse

    @GET("/method/audio.delete")
    suspend fun deleteTrackFromFavorites(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") account_owner_id: String,
        @Query("audio_id") audio_id: Int,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): TrackIdResponse

}