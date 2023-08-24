package com.kamancho.melisma.main.data.cloud


import com.google.errorprone.annotations.Keep
import com.kamancho.melisma.app.vkdto.TokenDto
import retrofit2.http.GET

import retrofit2.http.Query

/**
 * Created by HP on 08.03.2023.
 **/
@Keep
interface AuthorizationService {

    @Keep
    companion object{
        private const val GRAND_TYPE = "password"
        private const val CLIENT_ID = "6146827"
        private const val CLIENT_SECRET = "qVxWRF1CwHERuIrKBnqe"
        private const val API_VERS = "5.131"
        private const val TWO_FA_SUPPORT = "1"
    }

    @GET("/token")
    suspend fun getToken(
        @Query("grant_type")  grantType: String = GRAND_TYPE,
        @Query("client_id")  clientId: String = CLIENT_ID,
        @Query("client_secret")  clientSecret: String = CLIENT_SECRET,
        @Query("username")  username: String,
        @Query("password")  password: String,
        @Query("v")  apiVersion: String = API_VERS,
        @Query("2fa_supported")  twoFaSupported: String = TWO_FA_SUPPORT
    ): TokenDto
}