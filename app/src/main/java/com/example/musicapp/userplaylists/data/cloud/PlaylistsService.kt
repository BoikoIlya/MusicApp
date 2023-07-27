package com.example.musicapp.userplaylists.data.cloud

import com.example.musicapp.app.vkdto.FavoritesPlaylistsDto
import com.example.musicapp.app.vkdto.FollowPlaylistResponse
import com.example.musicapp.favorites.data.cloud.TrackIdResponse
import com.example.musicapp.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 12.07.2023.
 **/
interface PlaylistsService {

    companion object {
        private const val playlists_count = 200
    }

    @GET("/method/audio.getPlaylists")
    suspend fun getPlaylists(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("count") count: Int = playlists_count,
        @Query("v")  apiVersion: String = AppModule.api_version
    ):FavoritesPlaylistsDto

    @GET("/method/audio.followPlaylist")
    suspend fun followPlaylist(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: Int,
        @Query("playlist_id") playlist_id: Int,
        @Query("v")  apiVersion: String = AppModule.api_version
    ):FollowPlaylistResponse

    @GET("/method/audio.deletePlaylist")
    suspend fun deletePlaylist(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("playlist_id") playlist_id: Int,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): TrackIdResponse
}