package com.example.musicapp.search.data.cloud

import com.example.musicapp.app.SpotifyDto.SearchDto
import com.example.musicapp.app.SpotifyDto.SearchTracks
import com.example.testapp.spotifyDto.Recomendations
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by HP on 29.04.2023.
 **/
interface SearchTrackService {

    companion object{
        private const val content_market = "ES"
        private const val search_type = "track"
    }

    @GET("search")
    suspend fun searchTrack(
        @Header("Authorization") auth: String,
        @Query("type") type: String = search_type,
        @Query("q") query: String,
        @Query("market") market: String = content_market,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): SearchDto
}