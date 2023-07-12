package com.example.musicapp.playlist.data.cloud


import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by HP on 22.05.2023.
 **/
interface PlaylistService {

    companion object{
        private const val path ="playlists/{playlist_id}"
    }

//    @GET(path)
//    suspend fun fetchPlaylist(
//        @Header("Authorization") auth: String,
//        @Path("playlist_id") playlist_id: String,
//    ):PlaylistDto
}