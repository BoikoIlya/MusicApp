package com.kamancho.melisma.popular.data

import com.kamancho.melisma.app.vkdto.PopularCloud
import com.kamancho.melisma.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 22.08.2023.
 **/
interface PopularsService {

    @GET("/method/audio.getPopular")
    suspend fun getPopular(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v")  apiVersion: String = AppModule.api_version,
    ): PopularCloud

}