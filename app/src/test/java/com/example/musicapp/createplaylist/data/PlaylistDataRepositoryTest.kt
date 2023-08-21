package com.example.musicapp.createplaylist.data

import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.creteplaylist.data.PlaylistDataRepository
import com.example.musicapp.creteplaylist.data.cache.PlaylistDataCacheDataSource
import com.example.musicapp.creteplaylist.data.cloud.PlaylistDataCloudDataSource
import com.example.musicapp.trending.data.ObjectCreator
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 01.08.2023.
 **/
class PlaylistDataRepositoryTest: ObjectCreator() {

    private lateinit var repository: PlaylistDataRepository
    private lateinit var cache: TestPlaylistDataCacheDataSource
    private lateinit var cloud: TestPlaylistDataCloudDataSource


    @Before
    fun setup(){
        cache = TestPlaylistDataCacheDataSource()
        cloud = TestPlaylistDataCloudDataSource()
        repository = PlaylistDataRepository.Base(
            cloud =cloud,
            cache =cache,
            mapper = PlaylistItem.ToPlaylistCache()
        )

    }

    @Test
    fun `test create`() = runBlocking {
        val playlistId = 1
        cloud.returnItem = getPlaylistItem(playlistId)
        cloud.playlistTracks[playlistId] = emptyList()
        val idList = listOf(0,1)
        repository.createPlaylist("","", idList)

        assertEquals(idList.size,cache.playlistTracks[playlistId]!!.size)
        assertEquals(idList.size,cloud.playlistTracks[playlistId]!!.size)
        assertEquals(TestPlaylistDataCloudDataSource.PlaylistAction.Create,cloud.state.first())
    }

    @Test
    fun `test edit`() = runBlocking {
        val playlistId = 1
        cloud.returnItem = getPlaylistItem(playlistId)
        val initialList = listOf(0,1)
        cache.playlistTracks[playlistId] = initialList
        cloud.playlistTracks[playlistId] = initialList

        val idListToAdd = listOf(2,3)
        val idListToDelete = listOf(1)
        val expectedSize = initialList.size - idListToDelete.size+idListToAdd.size
        repository.editPlaylist(playlistId,"","", idListToAdd,idListToDelete)

        assertEquals(expectedSize,cache.playlistTracks[playlistId]!!.size)
        assertEquals(expectedSize,cloud.playlistTracks[playlistId]!!.size)
        assertEquals(TestPlaylistDataCloudDataSource.PlaylistAction.Edit,cloud.state.first())
        assertEquals(true,cache.isPlaylistUpdated)
    }


    @Test
    fun `test follow`() = runBlocking {
        val playlistId = 1
        cloud.returnItem = getPlaylistItem(playlistId)

        repository.followPlaylist(playlistId,0)
        assertEquals(cache.playlistTracks.size,cache.playlistTracks.size)
        assertEquals(cloud.playlistTracks.size,cloud.playlistTracks.size)
        assertEquals(TestPlaylistDataCloudDataSource.PlaylistAction.Follow,cloud.state.first())
    }

    class TestPlaylistDataCacheDataSource: PlaylistDataCacheDataSource {
        val playlistTracks = emptyMap<Int,List<Int>>().toMutableMap()
        var isPlaylistUpdated = false

        override suspend fun addTracksToPlaylist(playlistId: Int, audioIds: List<Int>) {
            val list = playlistTracks[playlistId]!!.toMutableList()
            list.addAll(audioIds)
            playlistTracks[playlistId] = list
        }

        override suspend fun removeTracksFromPlaylist(playlistId: Int, audioIds: List<Int>) {
            playlistTracks[playlistId] = playlistTracks[playlistId]!!.subtract(audioIds).toList()
        }

        override suspend fun insertPlaylist(data: PlaylistCache) {
            playlistTracks[data.playlistId] = emptyList()
        }

        override suspend fun updatePlaylist(playlist_id: Int, title: String, description: String) {
            isPlaylistUpdated = true
        }
    }

    class TestPlaylistDataCloudDataSource : PlaylistDataCloudDataSource,ObjectCreator() {
        val playlistTracks = emptyMap<Int,List<Int>>().toMutableMap()
        val state = listOf<PlaylistAction>().toMutableList()
        var returnItem = getPlaylistItem()

        enum class PlaylistAction{
            Create,Follow,Edit
        }

        override suspend fun createPlaylist(title: String, description: String): PlaylistItem {
            state.add(PlaylistAction.Create)
            return returnItem
        }

        override suspend fun followPlaylist(playlistId: Int, ownerId: Int): PlaylistItem {
            state.add(PlaylistAction.Follow)
            return returnItem
        }

        override suspend fun editPlaylist(playlistId: Int, title: String, description: String) {
            state.add(PlaylistAction.Edit)
        }

        override suspend fun addToPlaylist(playlistId: Int, audioIds: List<Int>) {
            val list = playlistTracks[playlistId]!!.toMutableList()
            list.addAll(audioIds)
            playlistTracks[playlistId] = list
        }

        override suspend fun removeFromPlaylist(playlistId: Int, audioIds: List<Int>) {
            playlistTracks[playlistId] = playlistTracks[playlistId]!!.subtract(audioIds).toList()
        }
    }
}