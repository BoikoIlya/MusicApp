package com.example.musicapp.trending.data.cloud

import com.example.musicapp.app.vkdto.TracksCloud
import com.example.musicapp.main.di.AppModule.Companion.api_version
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TrendingService{



    @GET("/method/audio.getRecommendations")
    suspend fun getRecommendations(
        @Query("access_token")  accessToken: String,
        @Query("owner_id")  owner_id: String,
        @Query("count") count: Int,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v")  apiVersion: String = api_version,
    ): TracksCloud


}