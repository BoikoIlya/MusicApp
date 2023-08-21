package com.example.musicapp.userplaylists.data.cloud

import com.example.musicapp.app.vkdto.FavoritesPlaylistsDto
import com.example.musicapp.app.vkdto.SearchPlaylistsCloud
import com.example.musicapp.main.di.AppModule
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 19.08.2023.
 **/
interface FriendPlaylistsService {

    @GET("/method/audio.getPlaylists")
    suspend fun getPlaylists(
        @Query("access_token")  accessToken: String,
        @Query("owner_id") owner_id: String,
        @Query("count") count: Int = PlaylistsService.playlists_count,
        @Query("v")  apiVersion: String = AppModule.api_version
    ): SearchPlaylistsCloud
}