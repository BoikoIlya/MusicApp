package com.example.musicapp.app.main.data.cloud

import TokenDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by HP on 08.03.2023.
 **/
interface AuthorizationService {

    @POST("api/token")
    @FormUrlEncoded
    suspend fun getToken(
        @Header("Authorization") auth: String,
        @Header("Content-Type") content: String,
        @Field(("grant_type")) grantType: String
    ): TokenDto
}