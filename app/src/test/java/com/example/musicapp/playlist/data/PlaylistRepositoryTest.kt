package com.example.musicapp.playlist.data

import com.example.musicapp.app.SpotifyDto.PlaylistDto
import com.example.musicapp.app.SpotifyDto.TrackItemPlaylist
import com.example.musicapp.main.data.AuthorizationRepositoryTest
import com.example.musicapp.playlist.data.cache.PlaylistIdTransfer
import com.example.musicapp.playlist.data.cloud.PlaylistService
import com.example.musicapp.trending.data.ObjectCreator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 23.05.2023.
 **/
class PlaylistRepositoryTest: ObjectCreator() {

    lateinit var playlistRepository: PlaylistRepository
    lateinit var transfer: PlaylistIdTransfer
    lateinit var tookenStore: AuthorizationRepositoryTest.TestTokenStore
    lateinit var service : TestPlaaylistService

    @Before
    fun setup(){
        service = TestPlaaylistService()
        transfer = PlaylistIdTransfer.Base()
        tookenStore = AuthorizationRepositoryTest.TestTokenStore()
        playlistRepository = PlaylistRepository.Base(
            tokenStore = tookenStore,
            service = service,
            mapper = PlaylistDto.ToPlaylistDomainMapper(TrackItemPlaylist.ToTrackDomainMapper()),
            transfer = transfer)
    }

    @Test
    fun `test fetch data`() = runBlocking {
        val tokenOrId = "111"
        transfer.save(tokenOrId)
        tookenStore.save(tokenOrId)
        val expectedObject = getPlaylistDataDomain()

       val actual = playlistRepository.fetchPlaylists()
        assertEquals(expectedObject,actual)
        assertEquals(tokenOrId,service.token)
        assertEquals(tokenOrId,transfer.read())
    }


    class TestPlaaylistService: PlaylistService, ObjectCreator(){
        var token = ""

        override suspend fun fetchPlaylist(auth: String, playlist_id: String): PlaylistDto {
            token = auth
            return getPlaylistDto()
        }
    }

}