package com.example.musicapp.trending.data

import com.example.musicapp.app.SpotifyDto.FetauredPlaylists
import com.example.musicapp.app.SpotifyDto.Item
import com.example.musicapp.trending.data.cloud.TrendingService
import com.example.musicapp.favorites.testcore.TokenStoreTest
import com.example.testapp.spotifyDto.Recomendations
import com.example.testapp.spotifyDto.Track
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 28.01.2023.
 **/
class TestTrendingRepository: ObjectCreator() {

    lateinit var repository: TrendingRepository
    lateinit var service: TestTrendingService

    @Before
    fun setup(){
        service = TestTrendingService()
        repository = TrendingRepository.Base(
            service = service,
            toPlaylistDomainMapper = Item.ToPlaylistsDomainMapper(),
            toTrackDomain = Track.ToTrackDomainMapper(),
            token = TokenStoreTest()
            )
    }

    @Test
    fun `test success`() = runBlocking {
       val actualPlaylists = repository.fetchPlaylists()
        val actualTracks = repository.fetchTracks()

        assertEquals(listOf(getPlaylistDomain()),actualPlaylists)
        assertEquals(listOf(getTrackDomain()),actualTracks)
    }


    class TestTrendingService: TrendingService, ObjectCreator(){

        override suspend fun getRecommendations(
            auth: String,
            limit: Int,
            market: String,
            seed_genres: String,
            seed_artists: String,
            seed_tracks: String,
        ): Recomendations {
         return getTracksResponse()
        }

        override suspend fun getFeaturedPlaylists(
            auth: String,
            limit: Int,
            offset: Int,
            timestamps: String,
            locale: String,
            country: String,
        ): FetauredPlaylists {
           return getPlaylistsResponse()
        }

    }
}