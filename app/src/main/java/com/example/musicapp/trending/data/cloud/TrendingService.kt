package com.example.musicapp.trending.data.cloud

import com.example.musicapp.app.SpotifyDto.FetauredPlaylists
import com.example.testapp.spotifyDto.Recomendations
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TrendingService{

    companion object{
        private const val limitResponse = 50
        private const val offsetResponse = 0
        private const val localeResponse = "sv_US"
        private const val countryResponse = "US"
        private const val seedArtists = "4NHQUGzhtTLFvgF5SZesLK"
        private const val seedTracks = "0c6xIDDpzE81m2q797ordA"
    }

    @GET("recommendations")
    suspend fun getRecommendations(
        @Header("Authorization") auth: String,
        @Query("limit") limit: Int = limitResponse,
        @Query("market") market: String,
        @Query("seed_genres") seed_genres: String,
        @Query("seed_artists ") seed_artists : String = seedArtists,
        @Query("seed_tracks") seed_tracks: String = seedTracks,
    ): Recomendations

    @GET("browse/featured-playlists")
    suspend fun getFeaturedPlaylists(
        @Header("Authorization") auth: String,
        @Query("limit") limit: Int = limitResponse,
        @Query("offset") offset: Int = offsetResponse,
        @Query("timestamps") timestamps: String,
        @Query("locale") locale: String = localeResponse,
        @Query("country") country: String = countryResponse,
    ): FetauredPlaylists
}