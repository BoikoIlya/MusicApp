package com.example.musicapp.favorites.data

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DuplicateException
import com.example.musicapp.app.core.FavoritesCacheDataSource
import com.example.musicapp.app.core.FavoritesCloudDataSource
import com.example.musicapp.app.core.FormatTimeSecondsToMinutesAndSeconds
import com.example.musicapp.app.core.HandleDeleteTrackRequest
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.ToMediaItemMapper
import com.example.musicapp.app.vkdto.Ads
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.favorites.data.cache.DomainToContainsMapper
import com.example.musicapp.favorites.data.cache.DomainToDataIdsMapper

import com.example.musicapp.favorites.testcore.TestManagerResource
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TrackDomainToCacheMapper
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.data.cloud.TracksCloudToCacheMapper
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.favorites.testcore.AccountDataStoreTest
import com.example.musicapp.trending.data.ObjectCreator
import com.example.musicapp.trending.domain.TrackDomain
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

/**
 * Created by HP on 28.04.2023.
 **/
class FavoritesReposotoryTest: ObjectCreator() {

    private lateinit var repository: FavoritesTracksRepository
    private lateinit var tracksDao: TestTracksDao
    private lateinit var managerResource: ManagerResource
    private lateinit var cache: TestFvoritesTracksCacheDataSource
    private lateinit var cloud: TestFvoritesTracksCloudDataSource

    @Before
    fun setup(){
        tracksDao = TestTracksDao()
        managerResource = TestManagerResource()
        cache = TestFvoritesTracksCacheDataSource()
        cloud = TestFvoritesTracksCloudDataSource()
        repository = FavoritesTracksRepository.Base(
            cache = cache,
            transfer = DataTransfer.MusicDialogTransfer(),
            domainToContainsMapper = DomainToContainsMapper.Base(TrackDomain.ContainsTrackMapper()),
            cloudToCacheMapper = TracksCloudToCacheMapper.Base(TrackItem.Mapper.CloudTrackToTrackCacheMapper()),
            cloud = cloud,
            domainToCacheMapper = TrackDomainToCacheMapper.Base(TrackDomain.ToTrackCacheMapper(FormatTimeSecondsToMinutesAndSeconds.Base())),
            domainToDataIdsMapper = DomainToDataIdsMapper.Base(TrackDomain.AddToFavoritesCloudMapper()),
            accountDataStore = AccountDataStoreTest(),
            handleResponseData = HandleDeleteTrackRequest(cache)
        )
    }





    @Test
    fun `test add To Favorites If Not Duplicated sucess`() = runBlocking {
        cache.list.addAll(listOf(getTrackCache()))
        val result1 = repository.addToFavoritesIfNotDuplicated(getTrackDomain(2))

        assertNotEquals(2, result1)
    }

    @Test(expected = DuplicateException::class)
    fun `test add To Favorites If Not Duplicated error`(): Unit = runBlocking {
        cache.list.addAll(listOf(getTrackCache(name = "1", artist = "1")))
        repository.addToFavoritesIfNotDuplicated(getTrackDomain(name = "1", author = "1"))

    }

    @Test
    fun `test add To Favorites`() = runBlocking {
        assertEquals(0,cache.list.size)
        val result = repository.addToFavorites(getTrackDomain(name = "1", author = "1"))
        assertNotEquals(2, result)
        assertEquals(1,cache.list.size)
    }

    @Test
    fun `test contains`() = runBlocking {
        val result1 = repository.containsInDb(Pair("1","1"))
        assertEquals(false, result1)

        cache.list.add(getTrackCache(1, name = "1", artist = "1"))
        val result2 = repository.containsInDb(Pair("1","1"))
        assertEquals(true, result2)
    }

    @Test
    fun `test delete`() = runBlocking {
        cloud.list.add(Pair(1,1))
        cache.list.add(getTrackCache(1))
        repository.deleteData(1)

        assertEquals(0, cache.list.size)
        assertEquals(0,cloud.list.size)
    }

    @Test
    fun `test update data`() = runBlocking {
        cloud.list.add(Pair(1,1))

        assertEquals(0,cache.list.size)

        repository.updateData()

        assertEquals(cloud.list.size,cache.list.size)
    }

    class TestTracksResultMapper(
        private val expectedList: List<MediaItem> = emptyList(),
        private val expectedMessage: String =""
    ): TracksResult.Mapper<Boolean>{

        override suspend fun map(
            message: String,
            list: List<MediaItem>,
            error: Boolean,
            newId: Int,
        ): Boolean {
            return message == expectedMessage
        }



    }

    class TestTracksDao: TracksDao{
        val list = emptyList<TrackCache>().toMutableList()

        override suspend fun insertTrack(track: TrackCache) {
           list.add(track)
        }

        override suspend fun insertListOfTracks(track: List<TrackCache>) {
            list.addAll(track)
        }

        override fun getAllTracksByTime(query: String, playlistId: Int): Flow<List<TrackCache>> = flow {
            emit(list.reversed())
        }

        override fun getTracksByName(query: String, playlistId: Int): Flow<List<TrackCache>> = flow {
            emit(list.sortedBy { it.name })
        }

        override fun getTracksByArtist(query: String, playlistId: Int): Flow<List<TrackCache>> = flow {
            emit(list.sortedBy { it.artistName })
        }


        override suspend fun contains(name: String, artistName: String): TrackCache? {
           return list.find { it.name ==name || it.artistName == artistName }
        }

        override suspend fun getById(id: Int): TrackCache? {
            return list.find { it.trackId == id }
        }

        override suspend fun removeTrack(id: Int) {
            list.removeIf { it.trackId == id }
        }

        override suspend fun deleteTracksNotInListWithSinglePlaylist(
            items: List<Int>,
            playlistId: Int,
        ) {
            list.removeAll { it.trackId  !in items }
        }



    }

    class TestFvoritesTracksCacheDataSource: FavoritesCacheDataSource<TrackCache>{
        val list = emptyList<TrackCache>().toMutableList()

        override suspend fun contains(nameAndAuthor: Pair<String, String>): Boolean {
           return list.find { it.name == nameAndAuthor.first && it.artistName == nameAndAuthor.second} !=null
        }

        override suspend fun removeFromDB(id: Int) {
            list.removeIf { it.trackId==id }
        }

        override suspend fun getById(id: Int): TrackCache {
            return list.find { it.trackId == id }?: throw NoSuchElementException()
        }

        override suspend fun update(list: List<TrackCache>) {
           this.list.addAll(list)
            this.list.removeAll { it !in list }
        }

        override suspend fun insert(trackCache: TrackCache) {
            list.add(trackCache)
        }

        override suspend fun insertWithNewId(id: Int, data: TrackCache) {
           list.add(data.copy(trackId = id))
        }

    }

    class TestFvoritesTracksCloudDataSource: FavoritesCloudDataSource<TrackItem>{
        val list = emptyList<Pair<Int,Int>>().toMutableList()

        override suspend fun addToFavorites(data: Pair<Int, Int>): Int {
            val newId = Random.nextInt()
            list.add(data.copy(first = newId))
            return newId
        }

        override suspend fun removeFromFavorites(id: Int) {
            list.removeIf { it.first == id }
        }

        override suspend fun favorites(): List<TrackItem> {
            return list.map { TrackItem(
                access_key = "1",
                ads = Ads(
                    account_age_type = "1",
                    content_id = "1",
                    duration = "1",
                    puid22 = "1"
                ),
                album = null,
                artist = "1",
                content_restricted = 0,
                date = 0,
                duration = 0,
                featured_artists = listOf(),
                genre_id = 0,
                id = it.first,
                is_explicit = false,
                is_focus_track = false,
                is_licensed = false,
                lyrics_id = 0,
                main_artists = listOf(),
                no_search = 0,
                owner_id = it.second,
                short_videos_allowed = false,
                stories_allowed = false,
                stories_cover_allowed = false,
                subtitle = null,
                title = "",
                track_code = "",
                url = ""
            ) }
        }

    }
}
