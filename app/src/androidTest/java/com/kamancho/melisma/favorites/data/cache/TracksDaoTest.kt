package com.kamancho.melisma.favorites.data.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.MusicDatabase
import com.kamancho.melisma.frienddetails.data.cache.FriendAndTracksRelation
import com.kamancho.melisma.frienddetails.data.cache.FriendsAndTracksRelationsDao
import com.kamancho.melisma.frienddetails.data.cache.FriendsDetailsCacheDataSource
import com.kamancho.melisma.friends.data.cache.FriendCache
import com.kamancho.melisma.friends.data.cache.FriendsDao
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import com.kamancho.melisma.userplaylists.data.cache.PlaylistDao
import com.kamancho.melisma.userplaylists.data.cache.PlaylistsAndTracksRelation
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
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
   private lateinit var tracksAndFriendsAndTracksRelationsDao: FriendsAndTracksRelationsDao
   private lateinit var friendDao: FriendsDao

   private lateinit var db: MusicDatabase
   private val listOfTracks = listOf(
       TrackCache(
           trackId = "0",
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
           trackId = "1",
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
           trackId = "2",
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
            playlistId = "1",
            title = "",
            is_following = false,
            count = 0,
            update_time = 0,
            description = "",
            owner_id = 0,
            thumbs = listOf()
        ),
        PlaylistCache(
            playlistId = "2",
            title = "",
            is_following = false,
            count = 0,
            update_time = 0,
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
        tracksAndFriendsAndTracksRelationsDao = db.getFriendsAndTracksRelationDao()
        friendDao = db.getFriendsDao()

        tracksDao.insertListOfTracks(listOfTracks)
        playlistsDao.insertListOfPlaylists(playlists)

    }

    @Test
    fun testDeleteTracksNotInListWithSinglePlaylist() = runBlocking {
        val relations = listOf(
            PlaylistsAndTracksRelation(playlistId = "1", trackId = "0"),
            PlaylistsAndTracksRelation(playlistId = "1", trackId = "1"),
            PlaylistsAndTracksRelation(playlistId = "2", trackId = "1"),
            PlaylistsAndTracksRelation(playlistId = "2", trackId = "2"),
        )

        playlistsAndTracksDao.insertRelationsList(relations)

        val deleteList = listOf("0","1")

//        val tracksOfPlaylist = tracksDao.tracksOfPlaylist(2).size
//        assertEquals(2,tracksOfPlaylist)
//
//        val tracksNotOfPlaylist = tracksDao.tracksNotOfPlaylist(2).size
//        assertEquals(2,tracksNotOfPlaylist)

        tracksDao.deleteTracksNotInListWithSinglePlaylist(deleteList,"2")
        assertEquals(2,tracksDao.getAllTracksByTime("","1").first().size)
        assertEquals(1,tracksDao.getAllTracksByTime("","2").first().size)
        assertEquals(null,tracksDao.getById(2))

        tracksDao.deleteTracksNotInListWithSinglePlaylist(listOf("2"),"1")
        assertEquals(null,tracksDao.getById(0))
    }

    @Test
    fun testDeleteTracksOfFriendNotInList() = runBlocking {
        friendDao.insertFriendsList(listOf(FriendCache(
            friendId = 0,
            firstName = "",
            secondName = "",
            photoUrl = ""
        )))

        tracksAndFriendsAndTracksRelationsDao.insertRelationsList(listOf(
            FriendAndTracksRelation(trackId = "0", friendId = 0),
            FriendAndTracksRelation(trackId = "1", friendId = 0),
            FriendAndTracksRelation(trackId = "2", friendId = 0),
        ))
        assertEquals(3,tracksDao.getTracksOfFriend("",0).first().size)
        tracksDao.deleteTracksOfFriendNotInList(listOf("0","1"),0)
        assertEquals(2,tracksDao.getTracksOfFriend("",0).first().size)

        tracksAndFriendsAndTracksRelationsDao.deleteRelationsNotInList(listOf("0","1"),0)
        assertEquals(2,tracksAndFriendsAndTracksRelationsDao.selectAll().size)
    }


    lateinit var friendDetailsCacheDataSource: FriendsDetailsCacheDataSource

    @Test
    fun testInsertTracks() = runBlocking {
        val trdao = TestTracksDao()
        val frrelDao = TestFriendsAndTracksRelationsDao()
        friendDetailsCacheDataSource = FriendsDetailsCacheDataSource.Base(
            tracksDao =trdao,
            playlistsDao = null,
            friendsAndTracksRelationsDao = frrelDao,
            friendsAndPlaylistsRelationDao = null,
            dispatchersList = DispatchersList.Base()
        )

        friendDetailsCacheDataSource.insertTracks(listOfTracks,"0")
        assertEquals(frrelDao.list.size,3)
        assertEquals(frrelDao.relationList.size,3)
        assertEquals(trdao.list.size,3)
        assertEquals(trdao.tracksList.size,3)
    }

    class TestFriendsAndTracksRelationsDao: FriendsAndTracksRelationsDao{
        val list = emptyList<String>().toMutableList()
        val relationList =  emptyList<FriendAndTracksRelation>().toMutableList()

        override suspend fun insertRelationsList(list: List<FriendAndTracksRelation>) {
            relationList.addAll(list)
        }

        override fun friendTrackCount(friendId: Int): Int {
            TODO("Not yet implemented")
        }

        override fun deleteRelationsNotInList(list: List<String>, friendId: Int) {
            this.list.addAll(list)
        }

        override suspend fun selectAll(): List<FriendAndTracksRelation> {
            TODO("Not yet implemented")
        }


    }


    class TestTracksDao: TracksDao{
        val list = emptyList<String>().toMutableList()
        val tracksList =  emptyList<TrackCache>().toMutableList()

        override suspend fun insertTrack(track: TrackCache) {

        }

        override suspend fun insertListOfTracks(track: List<TrackCache>) {
            tracksList.addAll(track)
        }

        override fun getAllTracksByTime(query: String, playlistId: String): Flow<List<TrackCache>> {
            TODO("Not yet implemented")
        }

        override fun getTracksByName(query: String, playlistId: String): Flow<List<TrackCache>> {
            TODO("Not yet implemented")
        }

        override fun getTracksByArtist(query: String, playlistId: String): Flow<List<TrackCache>> {
            TODO("Not yet implemented")
        }

        override suspend fun contains(
            name: String,
            artistName: String,
            mainPlaylistId: Int,
        ): TrackCache? {
            TODO("Not yet implemented")
        }

        override suspend fun getById(id: Int): TrackCache? {
            TODO("Not yet implemented")
        }

        override suspend fun removeTrack(id: Int) {
            TODO("Not yet implemented")
        }

        override fun getTracksOfFriend(
            query: String,
            friendId: Int,
        ): Flow<List<TrackCache>> {
            TODO("Not yet implemented")
        }

        override suspend fun deleteTracksNotInListWithSinglePlaylist(
            items: List<String>,
            playlistId: String,
        ) {
            TODO("Not yet implemented")
        }

        override suspend fun deleteTracksOfFriendNotInList(items: List<String>, friendId: Int) {
            list.addAll(items)
        }
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