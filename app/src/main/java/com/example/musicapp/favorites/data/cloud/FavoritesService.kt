package com.example.musicapp.favorites.data.cloud

import com.example.musicapp.app.vkdto.FavoritesTracks
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
        @Query("v")  apiVersion: String = "5.131",
    ): FavoritesTracks

    @GET("/method/audio.getCount")
    suspend fun getTracksCount(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("v")  apiVersion: String = "5.131"
    ): CountResponse
}