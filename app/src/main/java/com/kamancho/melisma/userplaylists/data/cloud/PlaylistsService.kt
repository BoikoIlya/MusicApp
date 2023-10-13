package com.kamancho.melisma.userplaylists.data.cloud

import com.kamancho.melisma.app.vkdto.CountResponse
import com.kamancho.melisma.app.vkdto.FavoritesPlaylistsDto
import com.kamancho.melisma.app.vkdto.FollowPlaylistResponse
import com.kamancho.melisma.app.vkdto.TrackIdResponse
import com.kamancho.melisma.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 12.07.2023.
 **/
interface PlaylistsService {

    companion object {
         const val playlists_count = 200
    }

    @GET("/method/audio.getPlaylists")
    suspend fun getPlaylists(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v")  apiVersion: String = AppModule.api_version,
        @Query("count") count: Int = playlists_count,
    ):FavoritesPlaylistsDto

    @GET("/method/audio.followPlaylist")
    suspend fun followPlaylist(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: Int,
        @Query("playlist_id") playlist_id: Int,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v")  apiVersion: String = AppModule.api_version
    ):FollowPlaylistResponse

    @GET("/method/audio.deletePlaylist")
    suspend fun deletePlaylist(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("playlist_id") playlist_id: Int,
        @Query("captcha_sid") captcha_sid:String,
        @Query("captcha_key") captcha_key:String,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): TrackIdResponse


}