package com.kamancho.melisma.friends.data.cloud

import com.kamancho.melisma.app.vkdto.FriendsCloud
import com.kamancho.melisma.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 17.08.2023.
 **/
interface FriendsService {

    companion object{
        private const val FIELDS: String = "photo_100"
    }

    @GET("/method/friends.get")
    suspend fun getFriends(
        @Query("access_token")  accessToken: String,
        @Query("offset") offset: Int,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("fields") fields: String = FIELDS,
        @Query("v")  apiVersion: String = AppModule.api_version,
    ): FriendsCloud
}