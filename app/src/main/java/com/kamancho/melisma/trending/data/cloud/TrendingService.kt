package com.kamancho.melisma.trending.data.cloud

import com.kamancho.melisma.app.vkdto.TracksCloud
import com.kamancho.melisma.main.di.AppModule.Companion.api_version
import retrofit2.http.GET
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