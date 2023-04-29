package com.example.musicapp.favorites.data.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.musicapp.app.core.MusicDatabase
import com.example.musicapp.searchhistory.data.cache.HistoryDao
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 29.04.2023.
 **/
class HistoryDaoTest {


    lateinit var dao: HistoryDao
    lateinit var db: MusicDatabase
    val list = listOf(
        HistoryItemCache(id = 0, queryTerm = "", time = 0)
    )

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MusicDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.getHistoryDao()

//        list.forEach {
//            dao.insertTrack(it)
//        }
    }


    @Test
    fun testRetriveHistoryByTime() = runBlocking {

//        val result = dao.getHistoryByTime().first()
//        assertEquals(,result)
    }
}