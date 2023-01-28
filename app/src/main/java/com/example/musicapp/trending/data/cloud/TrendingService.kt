package com.example.musicapp.trending.data.cloud

import com.example.musicapp.core.dto.Playlists
import com.example.musicapp.core.dto.TracksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 27.01.2023.
 **/
interface TrendingService {

    companion object{
        private const val playlists = "/v2.2/playlists"
        private const val apikey = "MzU5ZTdhNTItZTM5ZC00Y2I4LTg5ZWYtNzFlM2I5NDA3MDJi"
        private const val topTracks = "/v2.2/tracks/top"
    }

    @GET(playlists)
    suspend fun fetchPlaylists(
        @Query("apikey")
        apiKey:String = apikey
    ): Playlists

    @GET(topTracks)
    suspend fun fetchTop50Tracks(
        @Query("limit")
        limit: Int = 50,
    ): TracksResponse
}