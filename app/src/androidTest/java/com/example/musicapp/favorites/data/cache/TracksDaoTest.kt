package com.example.musicapp.favorites.data.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.musicapp.app.core.MusicDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 08.04.2023.
 **/
class TracksDaoTest {

   private lateinit var dao: TracksDao
   private lateinit var db: MusicDatabase
   private val list = listOf(
       TrackCache(
           id = "1",
           name = "a",
           artistName = "b",
           imgUrl = "",
           albumName = "",
           time = 0
       ),
       TrackCache(
           id = "2",
           name = "b",
           artistName = "c",
           imgUrl = "",
           albumName = "",
           time = 1
       ),
       TrackCache(
           id = "3",
           name = "c",
           artistName = "a",
           imgUrl = "",
           albumName = "",
           time = 2
       )
   )

    @Before
    fun setup()= runBlocking{
        val context = ApplicationProvider.getApplicationContext<Context>()
         db = Room.inMemoryDatabaseBuilder(context, MusicDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.getTracksDao()

        list.forEach {
            dao.insertTrack(it)
        }
    }


    @Test
    fun testGetTracksByTime() = runBlocking {

        val expected =  list.sortedByDescending { it.time }

        assertEquals(expected.first(), dao.searchTracksOrderByTime("").first().first())

    }

    @Test
    fun testGetTracksByName() = runBlocking {

        val expected =  list.sortedBy { it.name }

        assertEquals(expected.first(), dao.getTracksByName("").first().first())
    }

    @Test
    fun testGetTracksByAuthor() = runBlocking {

        val expected =  list.sortedBy { it.artistName }

        assertEquals(expected.first(), dao.getTracksByArtist("").first().first())
    }


    @Test
    fun testContains() = runBlocking{

        assertEquals(null, dao.contains(""))
        assertEquals(list.first(), dao.contains(list.first().id))
    }

    @Test
    fun testRemove() = runBlocking{

        dao.removeTrack(list.first().id)

        assertEquals(2,dao.searchTracksOrderByTime("").first().size)

    }

    @After
    fun teardown(){
        db.close()
    }
}