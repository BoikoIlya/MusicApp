package com.example.musicapp.creteplaylist.data.cloud

import com.example.musicapp.app.vkdto.FollowPlaylistResponse
import com.example.musicapp.favorites.data.cloud.TrackIdResponse
import com.example.musicapp.main.di.AppModule
import com.example.musicapp.userplaylists.data.cloud.PlaylistDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 17.07.2023.
 **/
interface PlaylistDataService {

    @GET("/method/audio.createPlaylist")
    suspend fun createPlaylist(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): PlaylistDataResponse


    @GET("/method/audio.editPlaylist")
    suspend fun editPlaylist(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("playlist_id") playlist_id: Int,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): TrackIdResponse

    @GET("/method/audio.followPlaylist")
    suspend fun followPlaylist(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: Int,
        @Query("playlist_id") playlist_id: Int,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): PlaylistDataResponse

    @GET("/method/audio.addToPlaylist")
    suspend fun addToPlaylist(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("playlist_id") playlist_id: Int,
        @Query("audio_ids") audio_ids: String,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): AddToPlaylistResponse

    @GET("/method/audio.removeFromPlaylist")
    suspend fun removeFromPlaylist(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("playlist_id") playlist_id: Int,
        @Query("audio_ids") audio_ids: String,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): TrackIdResponse
}