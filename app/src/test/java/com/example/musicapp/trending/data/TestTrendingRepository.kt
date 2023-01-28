package com.example.musicapp.trending.data

import com.example.musicapp.core.NoInternetConnectionException
import com.example.musicapp.core.dto.Playlist
import com.example.musicapp.core.dto.Playlists
import com.example.musicapp.core.dto.Track
import com.example.musicapp.core.dto.TracksResponse
import com.example.musicapp.trending.data.cloud.TrendingService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

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
            service =service,
            toPlaylistDomainMapper =Playlist.ToPlaylistDomain(),
            toTrackDomain =Track.ToTrackDomain(),
            handleResponse = HandleResponse.Base()
        )
    }

    @Test
    fun `test success`() = runBlocking {
       val actualPlaylists = repository.fetchPlaylists()
        val actualTracks = repository.fetchTracks()

        assertEquals(listOf(getPlaylistDomain()),actualPlaylists)
        assertEquals(listOf(getTrackDomain()),actualTracks)
    }

    @Test(expected = NoInternetConnectionException::class)
    fun `test error`() = runBlocking{
        service.throwException = true
        val actualPlaylists = repository.fetchPlaylists()
        val actualTracks = repository.fetchTracks()
    }


    class TestTrendingService: TrendingService, ObjectCreator(){
        var throwException = false

        override suspend fun fetchPlaylists(apiKey: String): Playlists {
            if(throwException) throw UnknownHostException()
            return getPlaylistsResponse()
        }

        override suspend fun fetchTop50Tracks(limit: Int): TracksResponse {
            if(throwException) throw UnknownHostException()
           return getTracksResponse()
        }

    }
}