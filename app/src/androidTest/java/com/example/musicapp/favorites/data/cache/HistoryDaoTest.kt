package com.example.musicapp.favorites.data.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.musicapp.app.core.MusicDatabase
import com.example.musicapp.searchhistory.data.cache.HistoryDao
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 29.04.2023.
 **/
class HistoryDaoTest {


    lateinit var dao: HistoryDao
    lateinit var db: MusicDatabase
    val list = listOf(
        HistoryItemCache(queryTerm = "aaa", time = 0),
        HistoryItemCache(queryTerm = "bbb", time = 1)
    )

    @Before
    fun setup() = runBlocking{
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MusicDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.getHistoryDao()

        list.forEach {
            dao.insertHistoryItem(it)
        }
    }


    @Test
    fun testRetriveHistoryByTime() = runBlocking {

        val result = dao.getAllHistoryByTime().first().first()
        assertEquals(list.last(),result)
    }

    @Test
    fun testSearch() = runBlocking {
        val newQuery = "ccc"

        val result1 = dao.searchHistory(list.first().queryTerm).first()
        assertEquals(listOf(list.first()),result1)

        val result2 =  dao.searchHistory(newQuery).first()
        assertEquals(emptyList<HistoryItemCache>(), result2)
    }

    @Test
    fun testDeleteItem() = runBlocking {
        val nonexistentId = "dlkngndoisfjg"

        dao.removeItem(list.first().queryTerm)
        val result1 =  dao.getAllHistoryByTime().first()
        assertEquals(listOf(list.last()),result1)

        dao.removeItem(nonexistentId)
        val result2 =  dao.getAllHistoryByTime().first()
        assertEquals(listOf(list.last()),result2)
    }

    @Test
    fun testDeleteAll() = runBlocking {
        dao.clearHistory()
        val result =  dao.getAllHistoryByTime().first()
        assertEquals(emptyList<HistoryItemCache>(),result)
    }

    @After
    fun teardown(){
        db.close()
    }
}