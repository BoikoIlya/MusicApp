package com.example.musicapp.favorites.data.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.musicapp.app.core.MusicDatabase
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.data.cache.PlaylistDao
import com.example.musicapp.userplaylists.data.cache.PlaylistsAndTracksRelation
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

   private lateinit var tracksDao: TracksDao
   private lateinit var playlistsDao: PlaylistDao
   private lateinit var playlistsAndTracksDao: PlaylistsAndTracksDao
   private lateinit var db: MusicDatabase
   private val list = listOf(
       TrackCache(
           trackId = 0,
           url = "",
           name = "",
           artistName = "",
           bigImgUrl = "",
           smallImgUrl = "",
           albumName = "",
           date = 0,
           durationFormatted = "",
           durationInMillis = 0.0f,
           ownerId = 0
       ),
       TrackCache(
           trackId = 1,
           url = "",
           name = "",
           artistName = "",
           bigImgUrl = "",
           smallImgUrl = "",
           albumName = "",
           date = 0,
           durationFormatted = "",
           durationInMillis = 0.0f,
           ownerId = 0
       ),
       TrackCache(
           trackId = 2,
           url = "",
           name = "",
           artistName = "",
           bigImgUrl = "",
           smallImgUrl = "",
           albumName = "",
           date = 0,
           durationFormatted = "",
           durationInMillis = 0.0f,
           ownerId = 0
       )
   )

    private val playlists = listOf(
        PlaylistCache(
            playlistId = 1,
            title = "",
            is_following = false,
            count = 0,
            create_time = 0,
            description = "",
            owner_id = 0,
            thumbs = listOf()
        ),
        PlaylistCache(
            playlistId = 2,
            title = "",
            is_following = false,
            count = 0,
            create_time = 0,
            description = "",
            owner_id = 0,
            thumbs = listOf()
        )
    )

    @Before
    fun setup()= runBlocking{
        val context = ApplicationProvider.getApplicationContext<Context>()
         db = Room.inMemoryDatabaseBuilder(context, MusicDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        tracksDao = db.getTracksDao()
        playlistsDao = db.getPlaylistDao()
        playlistsAndTracksDao = db.getPlaylistsAndTracksDao()

        tracksDao.insertListOfTracks(list)
        playlistsDao.insertListOfPlaylists(playlists)

    }

    @Test
    fun testDeleteTracksNotInListWithSinglePlaylist() = runBlocking {
        val relations = listOf(
            PlaylistsAndTracksRelation(playlistId = 1, trackId = 0),
            PlaylistsAndTracksRelation(playlistId = 1, trackId = 1),
            PlaylistsAndTracksRelation(playlistId = 2, trackId = 1),
            PlaylistsAndTracksRelation(playlistId = 2, trackId = 2),
        )

        playlistsAndTracksDao.insertRelationsList(relations)

        val deleteList = listOf(0,1)

//        val tracksOfPlaylist = tracksDao.tracksOfPlaylist(2).size
//        assertEquals(2,tracksOfPlaylist)
//
//        val tracksNotOfPlaylist = tracksDao.tracksNotOfPlaylist(2).size
//        assertEquals(2,tracksNotOfPlaylist)

        tracksDao.deleteTracksNotInListWithSinglePlaylist(deleteList,2)
        assertEquals(2,tracksDao.getAllTracksByTime("",1).first().size)
        assertEquals(1,tracksDao.getAllTracksByTime("",2).first().size)
        assertEquals(null,tracksDao.getById(2))

        tracksDao.deleteTracksNotInListWithSinglePlaylist(listOf(2),1)
        assertEquals(null,tracksDao.getById(0))
    }

//    @Test
//    fun testGetTracksByTime() = runBlocking {
//
//        val expected =  list.sortedByDescending { it.time }
//
//        assertEquals(expected.first(), dao.searchTracksOrderByTime("").first().first())
//
//    }
//
//    @Test
//    fun testGetTracksByName() = runBlocking {
//
//        val expected =  list.sortedBy { it.name }
//
//        assertEquals(expected.first(), dao.getTracksByName("").first().first())
//    }
//
//    @Test
//    fun testGetTracksByAuthor() = runBlocking {
//
//        val expected =  list.sortedBy { it.artistName }
//
//        assertEquals(expected.first(), dao.getTracksByArtist("").first().first())
//    }
//
//
//    @Test
//    fun testContains() = runBlocking{
//
//        assertEquals(null, dao.contains(""))
//        assertEquals(list.first(), dao.contains(list.first().id))
//    }
//
//    @Test
//    fun testRemove() = runBlocking{
//
//        dao.removeTrack(list.first().id)
//
//        assertEquals(2,dao.searchTracksOrderByTime("").first().size)
//
//    }

    @After
    fun teardown(){
        db.close()
    }
}