package com.example.musicapp.favorites.data.cache

import com.example.musicapp.app.core.FavoritesCacheDataSource
import com.example.musicapp.favorites.data.FavoritesReposotoryTest
import com.example.musicapp.trending.data.ObjectCreator
import com.example.musicapp.userplaylists.data.cache.PlaylistsAndTracksRelation
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 10.07.2023.
 **/
class FavoritesTracksCacheDataSourceTest: ObjectCreator() {

    private lateinit var cache: FavoritesCacheDataSource<TrackCache>
    private lateinit var tracksDao: FavoritesReposotoryTest.TestTracksDao
    private lateinit var tracksAndTracksRelationsDao: TestPlaylistsAndTracksRelationsDao
    private val items = listOf(getTrackCache(1,"1","1"),getTrackCache(2,"2","2"))

    @Before
    fun setup(){
        tracksAndTracksRelationsDao = TestPlaylistsAndTracksRelationsDao()
        tracksDao = FavoritesReposotoryTest.TestTracksDao()
        tracksDao.list.addAll(items)
        cache = BaseFavoritesTracksCacheDataSource(
            tracksDao = tracksDao,
            tracksAndTracksRelationsDao
        )
    }


    @Test
    fun `test contains`() = runBlocking {
       val actual = cache.contains(Pair(items.first().name,items.first().artistName))

        assertEquals(true,actual)
    }

    @Test
    fun `test get by id`() = runBlocking {
        val actual = cache.getById(items.first().trackId)

        assertEquals(items.first(),actual)
    }

    @Test
    fun `test insert`() = runBlocking {
        val item = getTrackCache(3,"3","3")
        cache.insert(item)

        assertEquals(item,tracksDao.list.last())
    }

    @Test
    fun `test insert with new id`() = runBlocking {
        val item = getTrackCache(3,"3","3")
        val newId = 4
        cache.insertWithNewId(newId,item)

        assertEquals(item.copy(trackId = newId),tracksDao.list.last())
        assertEquals(newId,tracksDao.list.last().trackId)
    }

    @Test
    fun `test remove`() = runBlocking {
        cache.removeFromDB(items.first().trackId)

        assertEquals(1,tracksDao.list.size)
    }

    @Test
    fun `test update`() = runBlocking {
        val newList = listOf(getTrackCache(3,"3","3"))
        cache.update(newList)

        assertEquals(1,tracksDao.list.size)
        assertEquals(newList.first(),tracksDao.list.first())
    }


    class TestPlaylistsAndTracksRelationsDao: PlaylistsAndTracksDao {
        val list =  emptyList<PlaylistsAndTracksRelation>()

        override suspend fun insertRelationsList(list: List<PlaylistsAndTracksRelation>) {
            this.list.toMutableList().addAll(list)
        }

        override suspend fun insertRelationDao(item: PlaylistsAndTracksRelation) {
            list.toMutableList().add(item)
        }

        override suspend fun deleteRelationsOfTrackIdsNotInList(
            trackIds: List<Int>,
            playlistId: Int,
        ) {
            list.toMutableList().removeIf { it.trackId !in trackIds && it.playlistId==playlistId }
        }

        override suspend fun deleteOneRelation(trackId: Int, playlistId: Int) {
            list.toMutableList().removeIf { it.trackId == trackId && it.playlistId==playlistId }
        }

        override suspend fun clearRelationsOfOnePlaylist(playlistId: Int) {
            list.toMutableList().removeIf { it.playlistId==playlistId }
        }

        override suspend fun deleteRelationsNotContainsInListOfPlaylistsIds(
            listIds: List<Int>,
            mainPlaylistId: Int,
        ) {
            list.toMutableList().removeIf { it.trackId !in listIds && it.playlistId!=mainPlaylistId }
        }

        override suspend fun deleteRelationInSignificantPlaylistThatContainsInIdsList(
            playlistId: Int,
            trackIds: List<Int>,
        ) {
            list.toMutableList().removeIf { it.trackId in trackIds && it.playlistId==playlistId }
        }
    }
}