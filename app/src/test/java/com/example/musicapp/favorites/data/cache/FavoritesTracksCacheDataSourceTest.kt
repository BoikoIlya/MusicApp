package com.example.musicapp.favorites.data.cache

import com.example.musicapp.app.core.FavoritesCacheDataSource
import com.example.musicapp.favorites.data.FavoritesReposotoryTest
import com.example.musicapp.trending.data.ObjectCreator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 10.07.2023.
 **/
class FavoritesTracksCacheDataSourceTest: ObjectCreator() {

    private lateinit var cache: FavoritesCacheDataSource<TrackCache>
    private lateinit var dao: FavoritesReposotoryTest.TestTracksDao
    private val items = listOf(getTrackCache(1,"1","1"),getTrackCache(2,"2","2"))

    @Before
    fun setup(){
        dao = FavoritesReposotoryTest.TestTracksDao()
        dao.list.addAll(items)
        cache = BaseFavoritesTracksCacheDataSource(
            dao = dao
        )
    }


    @Test
    fun `test contains`() = runBlocking {
       val actual = cache.contains(Pair(items.first().name,items.first().artistName))

        assertEquals(true,actual)
    }

    @Test
    fun `test get by id`() = runBlocking {
        val actual = cache.getById(items.first().id)

        assertEquals(items.first(),actual)
    }

    @Test
    fun `test insert`() = runBlocking {
        val item = getTrackCache(3,"3","3")
        cache.insert(item)

        assertEquals(item,dao.list.last())
    }

    @Test
    fun `test insert with new id`() = runBlocking {
        val item = getTrackCache(3,"3","3")
        val newId = 4
        cache.insertWithNewId(newId,item)

        assertEquals(item.copy(id = newId),dao.list.last())
        assertEquals(newId,dao.list.last().id)
    }

    @Test
    fun `test remove`() = runBlocking {
        cache.removeFromDB(items.first().id)

        assertEquals(1,dao.list.size)
    }

    @Test
    fun `test update`() = runBlocking {
        val newList = listOf(getTrackCache(3,"3","3"))
        cache.update(newList)

        assertEquals(1,dao.list.size)
        assertEquals(newList.first(),dao.list.first())
    }



}